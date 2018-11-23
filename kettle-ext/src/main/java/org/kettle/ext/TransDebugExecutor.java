package org.kettle.ext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.logging.KettleLogLayout;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.LogMessage;
import org.pentaho.di.core.logging.LoggingRegistry;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransAdapter;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.debug.BreakPointListener;
import org.pentaho.di.trans.debug.StepDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMeta;
import org.pentaho.di.trans.step.RowAdapter;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaDataCombi;
import org.pentaho.di.trans.step.StepStatus;
import org.springframework.util.StringUtils;

/**
 * @description: 转换调试执行程序
 * @author: ZX
 * @date: 2018/11/21 11:42
 */
public class TransDebugExecutor implements Runnable {

    private String executionId;
    private TransExecutionConfiguration executionConfiguration;
    private TransMeta transMeta = null;
    private TransDebugMeta transDebugMeta = null;
    private Trans trans = null;
    private Map<StepMeta, String> stepLogMap = new HashMap<StepMeta, String>();

    private TransDebugExecutor(TransExecutionConfiguration transExecutionConfiguration, TransMeta transMeta) {
        this.executionId = UUID.randomUUID().toString().replaceAll("-", "");
        this.executionConfiguration = transExecutionConfiguration;
        this.transMeta = transMeta;
    }

    private static Hashtable<String, TransDebugExecutor> executors = new Hashtable<String, TransDebugExecutor>();

    public static synchronized TransDebugExecutor initExecutor(TransExecutionConfiguration transExecutionConfiguration, TransMeta transMeta, TransDebugMeta transDebugMeta) {
        TransDebugExecutor transExecutor = new TransDebugExecutor(transExecutionConfiguration, transMeta);
        transExecutor.transDebugMeta = transDebugMeta;
        executors.put(transExecutor.getExecutionId(), transExecutor);
        return transExecutor;
    }

    public static TransDebugExecutor getExecutor(String executionId) {
        return executors.get(executionId);
    }

    public static void remove(String executionId) {
        executors.remove(executionId);
    }

    public String getExecutionId() {
        return executionId;
    }

    private boolean finished = false;
    private long errCount;

    public long getErrCount() {
        return errCount;
    }

    @Override
    public void run() {
        try {
            // Set the variables
            transMeta.injectVariables(executionConfiguration.getVariables());
            // Set the named parameters
            Map<String, String> paramMap = executionConfiguration.getParams();
            Set<String> keys = paramMap.keySet();
            for (String key : keys) {
                transMeta.setParameterValue(key, Const.NVL(paramMap.get(key), ""));
            }
            transMeta.activateParameters();

            // Set the arguments
            Map<String, String> arguments = executionConfiguration.getArguments();
            String[] argumentNames = arguments.keySet().toArray(new String[arguments.size()]);
            Arrays.sort(argumentNames);

            String[] args = new String[argumentNames.length];
            for (int i = 0; i < args.length; i++) {
                String argumentName = argumentNames[i];
                args[i] = arguments.get(argumentName);
            }
            boolean initialized = false;
            trans = new Trans(transMeta);

            trans.setPreview(true);
            trans.setSafeModeEnabled(executionConfiguration.isSafeModeEnabled());
            trans.setGatheringMetrics(executionConfiguration.isGatheringMetrics());
            trans.setLogLevel(executionConfiguration.getLogLevel());
            trans.setReplayDate(executionConfiguration.getReplayDate());
            trans.setRepository(executionConfiguration.getRepository());

            try {
                trans.prepareExecution(args);
                capturePreviewData(trans, transMeta.getSteps());
                initialized = true;
            } catch (KettleException e) {
                e.printStackTrace();
                checkErrorVisuals();
            }

            transDebugMeta.addRowListenersToTransformation(trans);
            transDebugMeta.addBreakPointListers(new BreakPointListener() {
                public void breakPointHit(TransDebugMeta transDebugMeta, StepDebugMeta stepDebugMeta, RowMetaInterface rowBufferMeta, List<Object[]> rowBuffer) {
                    showPreview(transDebugMeta, stepDebugMeta, rowBufferMeta, rowBuffer);
                }
            });

            if (trans.isReadyToStart() && initialized) {
                trans.addTransListener(new TransAdapter() {
                    public void transFinished(Trans trans) {
                        checkErrorVisuals();
                    }
                });

                trans.startThreads();

                while (!trans.isFinished())
                    Thread.sleep(500);

                errCount = trans.getErrors();
            } else {
                checkErrorVisuals();
                errCount = trans.getErrors();
            }

        } catch (Exception e) {
            e.printStackTrace();
            App.getInstance().getLog().logError("执行失败！", e);
        } finally {
            finished = true;
        }
    }

    public void capturePreviewData(Trans trans, List<StepMeta> stepMetas) {
        final StringBuffer loggingText = new StringBuffer();

        try {
            final TransMeta transMeta = trans.getTransMeta();

            for (final StepMeta stepMeta : stepMetas) {
                final RowMetaInterface rowMeta = transMeta.getStepFields(stepMeta).clone();
                previewMetaMap.put(stepMeta, rowMeta);
                final List<Object[]> rowsData = new LinkedList<Object[]>();

                previewDataMap.put(stepMeta, rowsData);
                previewLogMap.put(stepMeta, loggingText);

                StepInterface step = trans.findRunThread(stepMeta.getName());

                if (step != null) {

                    step.addRowListener(new RowAdapter() {
                        @Override
                        public void rowWrittenEvent(RowMetaInterface rowMeta, Object[] row) throws KettleStepException {
                            try {
                                rowsData.add(rowMeta.cloneRow(row));
                                if (rowsData.size() > 100) {
                                    rowsData.remove(0);
                                }
                            } catch (Exception e) {
                                throw new KettleStepException("Unable to clone row for metadata : " + rowMeta, e);
                            }
                        }
                    });
                }

            }
        } catch (Exception e) {
            loggingText.append(Const.getStackTracker(e));
        }

        trans.addTransListener(new TransAdapter() {
            @Override
            public void transFinished(Trans trans) throws KettleException {
                if (trans.getErrors() != 0) {
                    for (StepMetaDataCombi combi : trans.getSteps()) {
                        if (combi.copy == 0) {
                            StringBuffer logBuffer = KettleLogStore.getAppender().getBuffer(combi.step.getLogChannel().getLogChannelId(), false);
                            previewLogMap.put(combi.stepMeta, logBuffer);
                        }
                    }
                }
            }
        });
    }

    protected Map<StepMeta, RowMetaInterface> previewMetaMap = new HashMap<StepMeta, RowMetaInterface>();
    protected Map<StepMeta, List<Object[]>> previewDataMap = new HashMap<StepMeta, List<Object[]>>();
    protected Map<StepMeta, StringBuffer> previewLogMap = new HashMap<StepMeta, StringBuffer>();

    private void checkErrorVisuals() {
        if (trans.getErrors() > 0) {
            stepLogMap.clear();

            for (StepMetaDataCombi combi : trans.getSteps()) {
                if (combi.step.getErrors() > 0) {
                    String channelId = combi.step.getLogChannel().getLogChannelId();
                    List<KettleLoggingEvent> eventList = KettleLogStore.getLogBufferFromTo(channelId, false, 0, KettleLogStore.getLastBufferLineNr());
                    StringBuilder logText = new StringBuilder();
                    for (KettleLoggingEvent event : eventList) {
                        Object message = event.getMessage();
                        if (message instanceof LogMessage) {
                            LogMessage logMessage = (LogMessage) message;
                            if (logMessage.isError()) {
                                logText.append(logMessage.getMessage()).append(Const.CR);
                            }
                        }
                    }
                    stepLogMap.put(combi.stepMeta, logText.toString());
                }
            }

        } else {
            stepLogMap.clear();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public JsonArray getStepMeasure() throws Exception {
        JsonArray jsonArray = new JsonArray();

        if (executionConfiguration.isExecutingLocally()) {
            for (int i = 0; i < trans.nrSteps(); i++) {
                StepInterface baseStep = trans.getRunThread(i);
                StepStatus stepStatus = new StepStatus(baseStep);

                String[] fields = stepStatus.getTransLogFields();

                JsonArray childArray = new JsonArray();
                for (int f = 1; f < fields.length; f++) {
                    childArray.add(fields[f]);
                }
                jsonArray.add(childArray);
            }
        }

        return jsonArray;
    }

    public String getExecutionLog() throws Exception {

        if (executionConfiguration.isExecutingLocally()) {
            StringBuffer sb = new StringBuffer();
            KettleLogLayout logLayout = new KettleLogLayout(true);
            List<String> childIds = LoggingRegistry.getInstance().getLogChannelChildren(trans.getLogChannelId());
            List<KettleLoggingEvent> logLines = KettleLogStore.getLogBufferFromTo(childIds, true, -1, KettleLogStore.getLastBufferLineNr());
            for (int i = 0; i < logLines.size(); i++) {
                KettleLoggingEvent event = logLines.get(i);
                String line = logLayout.format(event).trim();
                sb.append(line).append("\n");
            }
            return sb.toString();
        }

        return "";
    }

    public JsonArray getStepStatus() throws Exception {
        JsonArray jsonArray = new JsonArray();

        HashMap<String, Integer> stepIndex = new HashMap<String, Integer>();
        if (executionConfiguration.isExecutingLocally()) {
            for (StepMetaDataCombi combi : trans.getSteps()) {
                Integer index = stepIndex.get(combi.stepMeta.getName());
                if (index == null) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.put("stepName", combi.stepMeta.getName());
                    int errCount = (int) combi.step.getErrors();
                    jsonObject.put("stepStatus", errCount);

                    if (errCount > 0) {
                        StringBuilder logText = new StringBuilder();
                        String channelId = combi.step.getLogChannel().getLogChannelId();
                        List<KettleLoggingEvent> eventList = KettleLogStore.getLogBufferFromTo(channelId, false, -1, KettleLogStore.getLastBufferLineNr());
                        for (KettleLoggingEvent event : eventList) {
                            Object message = event.getMessage();
                            if (message instanceof LogMessage) {
                                LogMessage logMessage = (LogMessage) message;
                                if (logMessage.isError()) {
                                    logText.append(logMessage.getMessage()).append(Const.CR);
                                }
                            }
                        }
                        jsonObject.put("logText", logText.toString());
                    }

                    stepIndex.put(combi.stepMeta.getName(), jsonArray.size());
                    jsonArray.add(jsonObject);
                } else {
                    JsonObject jsonObject = jsonArray.getJSONObject(index);
                    int errCount = (int) (combi.step.getErrors() + jsonObject.optInt("stepStatus"));
                    jsonObject.put("stepStatus", errCount);
                }
            }
        }

        return jsonArray;
    }

    public void stop() {
        trans.stopAll();
    }

    public void resume() {
        trans.resumeRunning();
    }

    private JsonObject jsonObject = new JsonObject();

    public synchronized void showPreview(TransDebugMeta transDebugMeta, StepDebugMeta stepDebugMeta, RowMetaInterface rowMeta, List<Object[]> rowsData) {
        List<ValueMetaInterface> valueMetas = rowMeta.getValueMetaList();

        JsonArray columns = new JsonArray();
        JsonObject metaData = new JsonObject();
        JsonArray fields = new JsonArray();
        for (int i = 0; i < valueMetas.size(); i++) {
            ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);
            fields.add(valueMeta.getName());
            String header = valueMeta.getComments() == null ? valueMeta.getName() : valueMeta.getComments();

            JsonObject column = new JsonObject();
            column.put("dataIndex", valueMeta.getName());
            column.put("header", header);
            column.put("width", 800 / valueMetas.size());
            columns.add(column);
        }
        metaData.put("fields", fields);
        metaData.put("root", "firstRecords");

        JsonArray firstRecords = new JsonArray();
        for (int rowNr = 0; rowNr < rowsData.size(); rowNr++) {
            Object[] rowData = rowsData.get(rowNr);
            JsonObject row = new JsonObject();
            for (int colNr = 0; colNr < rowMeta.size(); colNr++) {
                String string = null;
                ValueMetaInterface valueMetaInterface;
                try {
                    valueMetaInterface = rowMeta.getValueMeta(colNr);
                    if (valueMetaInterface.isStorageBinaryString()) {
                        Object nativeType = valueMetaInterface.convertBinaryStringToNativeType((byte[]) rowData[colNr]);
                        string = valueMetaInterface.getStorageMetadata().getString(nativeType);
                    } else {
                        string = rowMeta.getString(rowData, colNr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!StringUtils.hasText(string))
                    string = "&lt;null&gt;";

                ValueMetaInterface valueMeta = rowMeta.getValueMeta(colNr);
                row.put(valueMeta.getName(), string);
            }
            firstRecords.add(row);
        }

        jsonObject.put("metaData", metaData);
        jsonObject.put("columns", columns);
        jsonObject.put("firstRecords", firstRecords);

        rowsData.clear();
    }

    public boolean isPreviewed() {
        return jsonObject.size() == 3;
    }

    public void clearPreview() {
        jsonObject.clear();
    }

    public JsonObject getPreviewData() {
        return jsonObject;
    }

    public JsonArray getLastPreviewResults() {
        JsonArray previewSteps = new JsonArray();

        for (StepMeta stepMeta : transDebugMeta.getStepDebugMetaMap().keySet()) {
            StepDebugMeta stepDebugMeta = transDebugMeta.getStepDebugMetaMap().get(stepMeta);
            RowMetaInterface rowMeta = stepDebugMeta.getRowBufferMeta();
            List<Object[]> rowsData = stepDebugMeta.getRowBuffer();
            if (rowMeta == null || rowsData == null) {
                continue;
            }

            JsonArray columns = new JsonArray();
            JsonObject metaData = new JsonObject();
            JsonArray fields = new JsonArray();
            List<ValueMetaInterface> valueMetas = rowMeta.getValueMetaList();
            for (int i = 0; i < valueMetas.size(); i++) {
                ValueMetaInterface valueMeta = rowMeta.getValueMeta(i);
                fields.add(valueMeta.getName());
                String header = valueMeta.getComments() == null ? valueMeta.getName() : valueMeta.getComments();

                JsonObject column = new JsonObject();
                column.put("dataIndex", valueMeta.getName());
                column.put("header", header);
                column.put("width", 800 / valueMetas.size());
                columns.add(column);
            }
            metaData.put("fields", fields);
            metaData.put("root", "firstRecords");

            JsonArray firstRecords = new JsonArray();
            for (int rowNr = 0; rowNr < rowsData.size(); rowNr++) {
                Object[] rowData = rowsData.get(rowNr);
                JsonObject row = new JsonObject();
                for (int colNr = 0; colNr < rowMeta.size(); colNr++) {
                    String string = null;
                    ValueMetaInterface valueMetaInterface;
                    try {
                        valueMetaInterface = rowMeta.getValueMeta(colNr);
                        if (valueMetaInterface.isStorageBinaryString()) {
                            Object nativeType = valueMetaInterface.convertBinaryStringToNativeType((byte[]) rowData[colNr]);
                            string = valueMetaInterface.getStorageMetadata().getString(nativeType);
                        } else {
                            string = rowMeta.getString(rowData, colNr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!StringUtils.hasText(string))
                        string = "&lt;null&gt;";

                    ValueMetaInterface valueMeta = rowMeta.getValueMeta(colNr);
                    row.put(valueMeta.getName(), string);
                }
                firstRecords.add(row);
            }

            JsonObject stepPreviewInfo = new JsonObject();
            stepPreviewInfo.put("name", stepMeta.getName());
            stepPreviewInfo.put("metaData", metaData);
            stepPreviewInfo.put("columns", columns);
            stepPreviewInfo.put("firstRecords", firstRecords);

            previewSteps.add(stepPreviewInfo);
        }

        return previewSteps;
    }
}

package org.kettle.ext.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

/**
 * @description: 作业执行配置编解码器
 *
 * @author:   ZX
 * @date:     2018/11/20 18:30
 */
public class JobExecutionConfigurationCodec {

    public static JsonObject encode(JobExecutionConfiguration executionConfiguration) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("exec_local", executionConfiguration.isExecutingLocally() ? "Y" : "N");
        jsonObject.put("exec_remote", executionConfiguration.isExecutingRemotely() ? "Y" : "N");
        if (executionConfiguration.getRemoteServer() != null) {
            jsonObject.put("remote_server", executionConfiguration.getRemoteServer().getName());
        }
        jsonObject.put("pass_export", executionConfiguration.isPassingExport() ? "Y" : "N");

        JsonArray jsonArray = new JsonArray();
        List<String> paramNames = new ArrayList<String>(executionConfiguration.getParams().keySet());
        Collections.sort(paramNames);
        for (String name : paramNames) {
            String value = executionConfiguration.getParams().get(name);
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.put("name", name);
            jsonObject2.put("value", value);
            jsonArray.add(jsonObject2);
        }
        jsonObject.put("parameters", jsonArray);

        jsonArray = new JsonArray();
        List<String> variableNames = new ArrayList<String>(executionConfiguration.getVariables().keySet());
        Collections.sort(variableNames);
        for (String name : variableNames) {
            String value = executionConfiguration.getVariables().get(name);

            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.put("name", name);
            jsonObject2.put("value", value);
            jsonArray.add(jsonObject2);
        }
        jsonObject.put("variables", jsonArray);

        jsonArray = new JsonArray();
        List<String> argumentNames = new ArrayList<String>(executionConfiguration.getArguments().keySet());
        Collections.sort(argumentNames);
        for (String name : argumentNames) {
            String value = executionConfiguration.getArguments().get(name);
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.put("name", name);
            jsonObject2.put("value", value);
            jsonArray.add(jsonObject2);
        }
        jsonObject.put("arguments", jsonArray);

        jsonObject.put("replay_date", XMLHandler.date2string(executionConfiguration.getReplayDate()));
        jsonObject.put("safe_mode", executionConfiguration.isSafeModeEnabled() ? "Y" : "N");
        jsonObject.put("log_level", executionConfiguration.getLogLevel().getCode());
        jsonObject.put("clear_log", executionConfiguration.isClearingLog() ? "Y" : "N");

        jsonObject.put("start_copy_name", executionConfiguration.getStartCopyName());
        jsonObject.put("start_copy_nr", executionConfiguration.getStartCopyNr());

        jsonObject.put("gather_metrics", executionConfiguration.isGatheringMetrics() ? "Y" : "N");
        jsonObject.put("expand_remote_job", executionConfiguration.isExpandingRemoteJob() ? "Y" : "N");

        if (executionConfiguration.getPassedBatchId() != null) {
            jsonObject.put("passedBatchId", executionConfiguration.getPassedBatchId());
        }

        return jsonObject;
    }

    public static JobExecutionConfiguration decode(JsonObject jsonObject, AbstractMeta meta) throws ParserConfigurationException, SAXException, IOException {
        JobExecutionConfiguration executionConfiguration = new JobExecutionConfiguration();
        executionConfiguration.setExecutingLocally("Y".equalsIgnoreCase(jsonObject.optString("exec_local")));
        executionConfiguration.setExecutingRemotely("Y".equalsIgnoreCase(jsonObject.optString("exec_remote")));

        String remote_server = jsonObject.optString("remote_server");
        if (StringUtils.hasText(remote_server)) {
            executionConfiguration.setRemoteServer(meta.findSlaveServer(remote_server));
        }
        executionConfiguration.setPassingExport("Y".equalsIgnoreCase(jsonObject.optString("pass_export")));

        Map<String, String> map = new HashMap<String, String>();
        JsonArray jsonArray = jsonObject.optJSONArray("parameters");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject2 = jsonArray.getJSONObject(i);
            String paramName = jsonObject2.optString("name");
            String paramValue = jsonObject2.optString("value");
            String defaultValue = jsonObject2.optString("default_value");

            if (Const.isEmpty(paramValue)) {
                paramValue = Const.NVL(defaultValue, "");
            }
            map.put(paramName, paramValue);
        }
        executionConfiguration.setParams(map);

        jsonArray = jsonObject.optJSONArray("variables");
        map = new HashMap<String, String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject2 = jsonArray.getJSONObject(i);
            String varName = jsonObject2.optString("name");
            String varValue = jsonObject2.optString("value");

            if (!Const.isEmpty(varName)) {
                map.put(varName, varValue);
            }
        }
        executionConfiguration.setVariables(map);

        jsonArray = jsonObject.optJSONArray("arguments");
        map = new HashMap<String, String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject2 = jsonArray.getJSONObject(i);
            String varName = jsonObject2.optString("name");
            String varValue = jsonObject2.optString("value");
            if (!Const.isEmpty(varName)) {
                map.put(varName, varValue);
            }
        }
        executionConfiguration.setArguments(map);

        executionConfiguration.setReplayDate(XMLHandler.stringToDate(jsonObject.optString("replay_date")));
        executionConfiguration.setSafeModeEnabled("Y".equalsIgnoreCase(jsonObject.optString("safe_mode")));
        executionConfiguration.setLogLevel(LogLevel.getLogLevelForCode(jsonObject.optString("log_level")));
        executionConfiguration.setClearingLog("Y".equalsIgnoreCase(jsonObject.optString("clear_log")));

        executionConfiguration.setStartCopyName(jsonObject.optString("start_copy_name"));
        executionConfiguration.setStartCopyNr(jsonObject.optInt("start_copy_nr"));

        executionConfiguration.setGatheringMetrics("Y".equalsIgnoreCase(jsonObject.optString("gather_metrics")));
        executionConfiguration.setExpandingRemoteJob("Y".equalsIgnoreCase(jsonObject.optString("expand_remote_job")));

        if (jsonObject.containsKey("passedBatchId")) {
            executionConfiguration.setPassedBatchId(Long.parseLong(jsonObject.optString("passedBatchId")));
        }

        return executionConfiguration;
    }

}

package org.kettle.webapp.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kettle.ext.PluginFactory;
import org.kettle.ext.core.row.ValueMetaAndDataCodec;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.kettle.ext.utils.JsonUtils;
import org.kettle.ext.utils.SvgImageUrl;
import org.kettle.webapp.bean.Ext3Node;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.compress.CompressionProviderFactory;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.JobEntryPluginType;
import org.pentaho.di.core.plugins.PartitionerPluginType;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaPluginType;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.i18n.GlobalMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.job.entries.checkdbconnection.JobEntryCheckDbConnections;
import org.pentaho.di.job.entries.delay.JobEntryDelay;
import org.pentaho.di.job.entries.deletefolders.JobEntryDeleteFolders;
import org.pentaho.di.job.entries.evaluatetablecontent.JobEntryEvalTableContent;
import org.pentaho.di.job.entries.ftpsget.FTPSConnection;
import org.pentaho.di.job.entries.sftp.SFTPClient;
import org.pentaho.di.job.entries.simpleeval.JobEntrySimpleEval;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.laf.BasePropertyHandler;
import org.pentaho.di.trans.step.StepPartitioningMeta;
import org.pentaho.di.trans.steps.multimerge.MultiMergeJoinMeta;
import org.pentaho.di.trans.steps.randomvalue.RandomValueMeta;
import org.pentaho.di.trans.steps.randomvalue.RandomValueMetaFunction;
import org.pentaho.di.trans.steps.setvariable.SetVariableMeta;
import org.pentaho.di.trans.steps.systemdata.SystemDataTypes;
import org.pentaho.di.trans.steps.textfileoutput.TextFileOutputMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 系统主控制器
 * @author: ZX
 * @date: 2018/11/21 14:33
 */
@Controller
@RequestMapping(value = "/system")
public class SystemMainController {
//	public static JsonObject getStepAnnotationInfo(Class step,int index){
//		if(!step.isAnnotationPresent(Step.class)){
//			return null;
//      }
//		Step s=(Step)step.getAnnotation(Step.class);
//		JsonObject json=new JsonObject();
//		json.put("id", "step" +index);
//		json.put("text", PluginFactory.containBean(s.id()) ? s.name() : "<font color='red'>" + s.name() + "</font>");
//		json.put("pluginId",s.id());
//		json.put("icon","ui/images/"+s.image()+"?scale=32");
//		json.put("dragIcon","ui/images/"+s.image()+"?scale=32");
//		json.put("cls", "nav");
//		json.put("qtip",s.categoryDescription());
//		json.put("leaf", true);
//		return  json;
//	}

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/steps")
    protected void steps() throws ServletException, IOException {
        JsonArray jsonArray = new JsonArray();
        PluginRegistry registry = PluginRegistry.getInstance();
        final List<PluginInterface> baseSteps = registry.getPlugins(StepPluginType.class);
        final List<String> baseCategories = registry.getCategories(StepPluginType.class);
        int i = 0;
        for (String baseCategory : baseCategories) {
            List<PluginInterface> sortedCat = new ArrayList<PluginInterface>();
            for (PluginInterface baseStep : baseSteps) {
                if (baseStep.getCategory().equalsIgnoreCase(baseCategory)) {
                    sortedCat.add(baseStep);
                }
            }
            Collections.sort(sortedCat, new Comparator<PluginInterface>() {
                @Override
                public int compare(PluginInterface p1, PluginInterface p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            //若一级菜单下的叶子节点全部未实现则不显示出来
            boolean contains = false;
            for (PluginInterface p : sortedCat) {
                if (PluginFactory.containBean(p.getIds()[0])) {
                    contains = true;
                }
            }
            if (!contains) {
                continue;
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", "category" + i++);
            jsonObject.put("text", baseCategory);
            jsonObject.put("icon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("Folder_image")));
            jsonObject.put("cls", "nav-node");
            JsonArray children = new JsonArray();


            for (PluginInterface p : sortedCat) {
                if (!PluginFactory.containBean(p.getIds()[0])) {
                    continue;
                }
                String pluginName = p.getName();
                String pluginDescription = p.getDescription();

                JsonObject child = new JsonObject();
                child.put("id", "step" + i++);
                child.put("text", PluginFactory.containBean(p.getIds()[0]) ? pluginName : "<font color='red'>" + pluginName + "</font>");
                child.put("pluginId", p.getIds()[0]);
                child.put("icon", SvgImageUrl.getUrl(p));
                child.put("dragIcon", SvgImageUrl.getUrl(p));
                child.put("cls", "nav");
                child.put("qtip", pluginDescription);
                child.put("leaf", true);
                children.add(child);
                // if ( !filterMatch( pluginName ) && !filterMatch(
                // 	pluginDescription ) ) {
                // continue;
                // }
            }
            jsonObject.put("children", children);
            jsonArray.add(jsonObject);
        }
        //big data
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("id", "category" + i++);
        jsonObject.put("text", "big data");
        jsonObject.put("icon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("Folder_image")));
        jsonObject.put("cls", "nav-node");
        JsonArray children = new JsonArray();
        //HadoopFileInput
        JsonObject child1 = new JsonObject();
        child1.put("id", "step" + i++);
        child1.put("text", PluginFactory.containBean("HadoopFileInput") ? "Hadoop File Input" : "<font color='red'>" + "Hadoop File Input" + "</font>");
        child1.put("pluginId", "HadoopFileInput");
        child1.put("icon", "ui/images/HDI.svg?scale=32");
        child1.put("dragIcon", "ui/images/HDI.svg?scale=32");
        child1.put("cls", "nav");
        child1.put("qtip", "HadoopFileInputPlugin.Description");
        child1.put("leaf", true);
        children.add(child1);
        //HadoopFileOutput
        JsonObject child2 = new JsonObject();
        child2.put("id", "step" + i++);
        child2.put("text", PluginFactory.containBean("HadoopFileOutput") ? "Hadoop File Output" : "<font color='red'>" + "Hadoop File Output" + "</font>");
        child2.put("pluginId", "HadoopFileOutput");
        child2.put("icon", "ui/images/HDO.svg?scale=32");
        child2.put("dragIcon", "ui/images/HDO.svg?scale=32");
        child2.put("cls", "nav");
        child2.put("qtip", "i18n:org.pentaho.di.trans.step:BaseStep.TypeLongDesc.HadoopFileOutput");
        child2.put("leaf", true);
        children.add(child2);
        jsonObject.put("children", children);
        jsonArray.add(jsonObject);

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/jobentrys")
    protected void jobentrys() throws IOException {
        JsonArray jsonArray = new JsonArray();

        PluginRegistry registry = PluginRegistry.getInstance();
        final List<PluginInterface> baseJobEntries = registry.getPlugins(JobEntryPluginType.class);
        final List<String> baseCategories = registry.getCategories(JobEntryPluginType.class);

        int i = 0;
        for (String baseCategory : baseCategories) {

            List<PluginInterface> sortedCat = new ArrayList<>();
            for (PluginInterface baseJobEntry : baseJobEntries) {
                if (baseJobEntry.getIds()[0].equals(JobMeta.STRING_SPECIAL)) {
                    continue;
                }
                if (baseJobEntry.getCategory().equalsIgnoreCase(baseCategory)) {
                    sortedCat.add(baseJobEntry);
                }
            }
            //判断该一级节点下是否包含已经完成的主键字段
            boolean contains = false;
            for (PluginInterface p : sortedCat) {
                if (PluginFactory.containBean(p.getIds()[0])) {
                    contains = true;
                }
            }
            if (!contains) {
                continue;
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", "category" + i++);
            jsonObject.put("text", baseCategory);
            jsonObject.put("icon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("Folder_image")));
            jsonObject.put("cls", "nav-node");
            JsonArray children = new JsonArray();

            if (baseCategory.equalsIgnoreCase(JobEntryPluginType.GENERAL_CATEGORY)) {
                JobEntryCopy startEntry = JobMeta.createStartEntry();
                JsonObject child = new JsonObject();
                child.put("id", startEntry.getEntry().getPluginId());
                child.put("text", startEntry.getName());
                child.put("pluginId", startEntry.getEntry().getPluginId());
                child.put("icon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("STR_image")));
                child.put("dragIcon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("STR_image")));
                child.put("cls", "nav");
                child.put("qtip", startEntry.getDescription());
                child.put("leaf", true);
                children.add(child);

                JobEntryCopy dummyEntry = JobMeta.createDummyEntry();
                child = new JsonObject();
                child.put("id", "step" + i++);
                child.put("text", dummyEntry.getName());
                child.put("pluginId", dummyEntry.getEntry().getPluginId());
                child.put("icon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("DUM_image")));
                child.put("dragIcon", SvgImageUrl.getUrl(BasePropertyHandler.getProperty("DUM_image")));
                child.put("cls", "nav");
                child.put("qtip", dummyEntry.getDescription());
                child.put("leaf", true);
                children.add(child);
            }

            Collections.sort(sortedCat, new Comparator<PluginInterface>() {
                @Override
                public int compare(PluginInterface p1, PluginInterface p2) {
                    return p1.getName().compareTo(p2.getName());
                }
            });
            for (PluginInterface p : sortedCat) {
                if (!PluginFactory.containBean(p.getIds()[0])) {
                    continue;
                }
                String pluginName = p.getName();
                String pluginDescription = p.getDescription();

                JsonObject child = new JsonObject();
                child.put("id", "step" + i++);
                child.put("text", PluginFactory.containBean(p.getIds()[0]) ? pluginName : "<font color='red'>" + pluginName + "</font>");
                child.put("pluginId", p.getIds()[0]);
                child.put("icon", SvgImageUrl.getUrl(p));
                child.put("dragIcon", SvgImageUrl.getUrl(p));
                child.put("cls", "nav");
                child.put("qtip", pluginDescription);
                child.put("leaf", true);
                children.add(child);
                // if ( !filterMatch( pluginName ) && !filterMatch(
                // 	pluginDescription ) ) {
                // continue;
                // }
            }
            jsonObject.put("children", children);
            jsonArray.add(jsonObject);
        }
        JsonUtils.response(jsonArray);
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/systemDataTypes")
    protected void systemDataTypes() throws IOException {
        JsonArray jsonArray = new JsonArray();

        SystemDataTypes[] values = SystemDataTypes.values();
        for (SystemDataTypes value : values) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", value.getCode());
            jsonObject.put("descrp", value.getDescription());
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/randomValueFunc")
    protected void randomValueFunc() throws IOException {
        JsonArray jsonArray = new JsonArray();

        RandomValueMetaFunction[] values = RandomValueMeta.functions;
        for (RandomValueMetaFunction value : values) {
            if (value == null) {
                continue;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("type", value.getType());
            jsonObject.put("code", value.getCode());
            jsonObject.put("descrp", value.getDescription());
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/valueMeta")
    protected void valueMeta() throws IOException {
        JsonArray jsonArray = new JsonArray();

        PluginRegistry pluginRegistry = PluginRegistry.getInstance();
        List<PluginInterface> plugins = pluginRegistry.getPlugins(ValueMetaPluginType.class);
        for (PluginInterface plugin : plugins) {
            int id = Integer.valueOf(plugin.getIds()[0]);
            if (id > 0 && id != ValueMetaInterface.TYPE_SERIALIZABLE) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.put("id", id);
                jsonObject.put("name", plugin.getName());
                jsonArray.add(jsonObject);
            }
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/valueFormat")
    protected void valueFormat(@RequestParam String valueType) throws IOException {
        JsonArray jsonArray = new JsonArray();
        if ("all".equalsIgnoreCase(valueType)) {
            for (String format : Const.getConversionFormats()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.put("name", format);
                jsonArray.add(jsonObject);
            }
        } else {
            int type = ValueMeta.getType(valueType);
            if (type == ValueMetaInterface.TYPE_INTEGER || type == ValueMetaInterface.TYPE_NUMBER) {
                String[] fmt = Const.getNumberFormats();
                for (String str : fmt) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.put("id", ValueMeta.getType(str));
                    jsonObject.put("name", str);
                    jsonArray.add(jsonObject);
                }
            } else if (type == ValueMetaInterface.TYPE_DATE || type == ValueMetaInterface.TYPE_TIMESTAMP) {
                String[] fmt = Const.getDateFormats();
                for (String str : fmt) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.put("id", ValueMeta.getType(str));
                    jsonObject.put("name", str);
                    jsonArray.add(jsonObject);
                }
            }
        }
        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/valueString")
    protected void valueString(HttpServletRequest request, HttpServletResponse response, @RequestParam String valueMeta) throws Exception {
        JsonObject jsonObject = JsonObject.fromObject(valueMeta);

        ValueMetaAndData valueMetaAndData = ValueMetaAndDataCodec.decode(jsonObject);
        String value = valueMetaAndData.toString();

        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(value);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/func")
    protected void func() throws Exception {

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < Condition.functions.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", Condition.functions[i]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/datetimeformat")
    protected void datetimeformat() throws Exception {
        JsonArray jsonArray = new JsonArray();
        String[] dats = Const.getDateFormats();
        for (int x = 0; x < dats.length; x++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", dats[x]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/formatMapperLineTerminator")
    protected void formatMapperLineTerminator() throws Exception {
        JsonArray jsonArray = new JsonArray();
        String[] dats = TextFileOutputMeta.formatMapperLineTerminator;
        for (int x = 0; x < dats.length; x++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", dats[x]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/compressionProviderNames")
    protected void compressionProviderNames() throws Exception {
        JsonArray jsonArray = new JsonArray();
        String[] dats = CompressionProviderFactory.getInstance().getCompressionProviderNames();
        for (int x = 0; x < dats.length; x++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", dats[x]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/availableCharsets")
    protected void availableCharsets() throws Exception {
        JsonArray jsonArray = new JsonArray();
        Collection<Charset> dats = Charset.availableCharsets().values();
        for (Charset charset : dats) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", charset.displayName());
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/locale")
    protected void locale() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < GlobalMessages.localeCodes.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", GlobalMessages.localeCodes[i]);
            jsonObject.put("desc", GlobalMessages.localeDescr[i]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/connectiontype")
    protected void connectiontype() throws Exception {
        JsonArray jsonArray = new JsonArray();
        String[] connection_type_Descs = FTPSConnection.connection_type_Desc;
        for (String charset : connection_type_Descs) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", charset);
            jsonArray.add(jsonObject);
        }
        JsonUtils.response(jsonArray);
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/proxyType")
    protected void proxyType() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (String str : new String[]{SFTPClient.PROXY_TYPE_HTTP, SFTPClient.PROXY_TYPE_SOCKS5}) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", str);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/multijointype")
    protected void multijointype() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (String str : MultiMergeJoinMeta.join_types) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("name", str);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/timeunit")
    protected void timeunit() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < JobEntryCheckDbConnections.unitTimeCode.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", JobEntryCheckDbConnections.unitTimeCode[i]);
            jsonObject.put("desc", JobEntryCheckDbConnections.unitTimeDesc[i]);
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/timeunit2")
    @ResponseBody
    protected List timeunit2() throws Exception {
        ArrayList list = new ArrayList();

        LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
        record.put("code", "0");
        record.put("desc", BaseMessages.getString(JobEntryDelay.class, "JobEntryDelay.SScaleTime.Label"));
        list.add(record);

        record = new LinkedCaseInsensitiveMap();
        record.put("code", "1");
        record.put("desc", BaseMessages.getString(JobEntryDelay.class, "JobEntryDelay.MnScaleTime.Label"));
        list.add(record);

        record = new LinkedCaseInsensitiveMap();
        record.put("code", "2");
        record.put("desc", BaseMessages.getString(JobEntryDelay.class, "JobEntryDelay.HrScaleTime.Label"));
        list.add(record);

        return list;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/successCondition")
    protected void successCondition() throws Exception {
        JsonArray jsonArray = new JsonArray();
        String[] successConditionsCode = JobEntryEvalTableContent.successConditionsCode;
        for (int i = 0; i < successConditionsCode.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("value", successConditionsCode[i]);
            jsonObject.put("text", JobEntryEvalTableContent.getSuccessConditionDesc(i));
            jsonArray.add(jsonObject);
        }
        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/logLevel")
    protected void logLevel() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (LogLevel level : new LogLevel[]{LogLevel.NOTHING, LogLevel.ERROR, LogLevel.MINIMAL,
                LogLevel.BASIC, LogLevel.DETAILED, LogLevel.DEBUG, LogLevel.ROWLEVEL}) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", level.getLevel());
            jsonObject.put("code", level.getCode());
            jsonObject.put("desc", level.getDescription());
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/variableType")
    protected void variableType() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < SetVariableMeta.getVariableTypeDescriptions().length; i++) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("id", i);
            jsonObject.put("code", SetVariableMeta.getVariableTypeCode(i));
            jsonObject.put("desc", SetVariableMeta.getVariableTypeDescription(i));
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/partitionMethod")
    protected void partitionMethod() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < StepPartitioningMeta.methodCodes.length; i++) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", StepPartitioningMeta.methodCodes[i]);
            jsonObject.put("desc", StepPartitioningMeta.methodDescriptions[i]);
            jsonArray.add(jsonObject);
        }

        PluginRegistry registry = PluginRegistry.getInstance();
        List<PluginInterface> plugins = registry.getPlugins(PartitionerPluginType.class);
        for (PluginInterface plugin : plugins) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("code", plugin.getIds()[0]);
            jsonObject.put("desc", plugin.getDescription());
            jsonArray.add(jsonObject);
        }

        JsonUtils.response(jsonArray);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/filextension")
    @ResponseBody
    protected List filextension(@RequestParam int extension) throws Exception {
        return FileNodeType.toList(extension);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/fileexplorer")
    @ResponseBody
    protected List fileexplorer(@RequestParam String path, @RequestParam int extension) throws Exception {
        LinkedList directorys = new LinkedList();
        LinkedList leafs = new LinkedList();
        if (StringUtils.hasText(path)) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isHidden()) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        directorys.addLast(Ext3Node.initNode(file.getAbsolutePath(), file.getName()));
                    } else if (file.isFile() && FileNodeType.match(FileNodeType.getExtension(file.getName()), extension)) {
                        leafs.addLast(Ext3Node.initNode(file.getAbsolutePath(), file.getName(), true));
                    }
                }
            }
        } else {
            File[] files = File.listRoots();
            for (File file : files) {
                if (file.isDirectory()) {
                    directorys.addLast(Ext3Node.initNode(file.getAbsolutePath(), file.getCanonicalPath()));
                } else if (file.isFile() && FileNodeType.match(FileNodeType.getExtension(file.getName()), extension)) {
                    leafs.addLast(Ext3Node.initNode(file.getAbsolutePath(), file.getCanonicalPath(), true));
                }
            }
        }

        directorys.addAll(leafs);
        return directorys;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deleteFoldersSuccessCondition")
    @ResponseBody
    protected List deleteFoldersSuccessCondition() throws Exception {
        ArrayList list = new ArrayList();

        LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
        record.put("code", "success_when_at_least");
        record.put("desc", BaseMessages.getString(JobEntryDeleteFolders.class, "JobDeleteFolders.SuccessWhenAtLeat.Label"));
        list.add(record);

        record = new LinkedCaseInsensitiveMap();
        record.put("code", "success_if_errors_less");
        record.put("desc", BaseMessages.getString(JobEntryDeleteFolders.class, "JobDeleteFolders.SuccessWhenErrorsLessThan.Label"));
        list.add(record);

        record = new LinkedCaseInsensitiveMap();
        record.put("code", "success_if_no_errors");
        record.put("desc", BaseMessages.getString(JobEntryDeleteFolders.class, "JobDeleteFolders.SuccessWhenAllWorksFine.Label"));
        list.add(record);

        return list;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/successConditionForSimp")
    protected void successConditionForSimp() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < JobEntrySimpleEval.successConditionCode.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("value", JobEntrySimpleEval.successConditionCode[i]);
            jsonObject.put("text", JobEntrySimpleEval.successConditionDesc[i]);
            jsonArray.add(jsonObject);
        }
        JsonUtils.response(jsonArray);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/successNumberCondition")
    protected void successNumberCondition() throws Exception {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < JobEntrySimpleEval.successNumberConditionCode.length; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("value", JobEntrySimpleEval.successNumberConditionCode[i]);
            jsonObject.put("text", JobEntrySimpleEval.successNumberConditionDesc[i]);
            jsonArray.add(jsonObject);
        }
        JsonUtils.response(jsonArray);
    }
}

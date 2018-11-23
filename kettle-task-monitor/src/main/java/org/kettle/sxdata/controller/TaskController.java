package org.kettle.sxdata.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kettle.ext.JobExecutor;
import org.kettle.ext.PluginFactory;
import org.kettle.ext.TransExecutor;
import org.kettle.ext.base.GraphCodec;
import org.kettle.ext.job.JobExecutionConfigurationCodec;
import org.kettle.ext.trans.TransExecutionConfigurationCodec;
import org.kettle.ext.utils.JsonObject;
import org.kettle.ext.utils.JsonUtils;
import org.kettle.ext.utils.RepositoryUtils;
import org.kettle.ext.utils.StringEscapeHelper;
import org.kettle.sxdata.entity.*;
import org.kettle.sxdata.service.ControlService;
import org.kettle.sxdata.service.JobService;
import org.kettle.sxdata.service.SlaveService;
import org.kettle.sxdata.service.TransService;
import org.kettle.sxdata.util.common.StringDateUtil;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;


/**
 * @description: 任务控制器
 * @author: ZX
 * @date: 2018/11/20 11:48
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController {

    @Autowired
    protected TransService transService;
    @Autowired
    protected JobService jobService;
    @Autowired
    protected SlaveService slaveService;
    @Autowired
    protected ControlService controlService;
    private static HashMap<String, JobExecutor> executions = new HashMap<>();

    /**
     * 修改任务名
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/updateTaskName")
    @ResponseBody
    protected void updateTaskName(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            boolean isSuccess = false;
            String oldName = request.getParameter("oldName");
            String newName = request.getParameter("newName");
            String type = request.getParameter("type");
            if (type.equals("job")) {
                isSuccess = jobService.updateJobName(oldName, newName);
            } else {
                isSuccess = transService.updateTransName(oldName, newName);
            }
            PrintWriter out = response.getWriter();
            String result = "";
            if (isSuccess) {
                result = "OK";
            } else {
                result = "faile";
            }
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 暂停/开始转换
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/pauseOrStart")
    @ResponseBody
    protected void pauseOrStart(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String[] idArray = request.getParameterValues("idArray");
            String[] portArray = request.getParameterValues("portArray");
            controlService.pauseOrStartTrans(idArray, portArray);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"success\":true}");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 停止转换/作业
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/stopTransOrJob")
    @ResponseBody
    protected void stopTransOrJob(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String[] typeArray = request.getParameterValues("typeArray");
            String[] idArray = request.getParameterValues("idArray");
            String[] hostArray = request.getParameterValues("hostArray");
            for (int i = 0; i < typeArray.length; i++) {
                if (typeArray[i].trim().equals("作业")) {
                    controlService.stopJob(hostArray[i], idArray[i]);
                } else {
                    controlService.stopTrans(hostArray[i], idArray[i]);
                }
            }
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"success\":true}");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 根据作业/转换名获取作业/转换
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getJobOrTransByName")
    @ResponseBody
    protected void getJobOrTransByName(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String type = request.getParameter("type");
            String taskName = request.getParameter("taskName");
            String result = "";
            if (type.equals("job")) {
                JobEntity job = jobService.getJobByName(taskName);
                result = JSONObject.fromObject(job).toString();
            } else if (type.equals("trans")) {
                TransformationEntity trans = transService.getTransByName(taskName);
                result = JSONObject.fromObject(trans).toString();
            }
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取转换的详情列表
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getTransDetail")
    @ResponseBody
    protected void getTransDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String carteId = request.getParameter("carteId");
            String hostName = request.getParameter("hostName");
            List<StepStatus> lists = controlService.getTransDetail(carteId, hostName);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONArray.fromObject(lists).toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取某个任务的日志
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getLog")
    @ResponseBody
    protected void getLog(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String result = "";
            String id = request.getParameter("id");
            String type = request.getParameter("type");
            String hostName = request.getParameter("hostName");
            if (type.trim().equals("作业")) {
                result = controlService.getLogDetailForJob(id, hostName);
            } else if (type.trim().equals("转换")) {
                result = controlService.getLogDetailForTrans(id, hostName);
            }
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取所有正在运行中的任务(transformation job)
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getRunningTask")
    @ResponseBody
    protected void getRunningTask(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
            String userGroupName = "";
            if (null != attr) {
                userGroupName = attr.getUserGroupName();
            }
            List<TaskControlEntity> jobItems = controlService.getAllRunningJob(userGroupName);
            for (TaskControlEntity item : controlService.getAllRunningTrans(userGroupName)) {
                jobItems.add(item);
            }
            String result = JSONArray.fromObject(jobItems).toString();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 查询作业;包括条件查询
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getJobs.do")
    @ResponseBody
    protected void getJobs(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取前台传递的分页参数
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            //获取前台传递的查询参数 作业名以及创建时间 如果为空则代表是全部查询
            String name = request.getParameter("name");
            String createDate = request.getParameter("date");
            //获取当前用户所在的用户组
            UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
            String userGroupName = "";
            if (null != attr) {
                userGroupName = attr.getUserGroupName();
            }
            JSONObject result = jobService.findJobs(start, limit, name, createDate, userGroupName);

            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除作业OR转换
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    protected void deleteJobs(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String path = request.getParameter("path");
            String flag = request.getParameter("flag");
            //判断是需要删除转换还是需要删除作业
            if (flag.equals("transformation")) {
                transService.deleteTransformation(path, flag);
            } else if (flag.equals("job")) {
                jobService.deleteJobs(path, flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    /**
     * 查询转换;包括条件查询
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/getTrans")
    protected void getTrans(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取前台传递的分页参数
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));
            //获取前台传递的查询参数转换名以及创建时间 如果两个参数为空则代表是查询全部
            String transName = request.getParameter("name");
            String createDate = request.getParameter("date");
            //获取当前用户所在的用户组
            UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
            String userGroupName = "";
            if (null != attr) {
                userGroupName = attr.getUserGroupName();
            }

            JSONObject result = transService.findTrans(start, limit, transName, createDate, userGroupName);
            //输出结果返回给客户端
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 在节点上执行转换OR作业
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/execute")
    protected void execute(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String path = request.getParameter("path");
            Integer slaveId = Integer.valueOf(request.getParameter("slaveId"));
            String flag = request.getParameter("flag");
            if (flag.equals("job")) {
                jobService.executeJob(path, slaveId);
            } else if (flag.equals("transformation")) {
                transService.executeTransformation(path, slaveId);
            }
            //输出结果返回给客户端
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("......");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 智能执行转换OR作业
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/powerExecute")
    protected void powerExecute(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String path = request.getParameter("path");
        String flag = request.getParameter("powerFlag");
        //在所有节点中获取负载最低的节点
        UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
        String userGroupName = "";
        if (null != attr) {
            userGroupName = attr.getUserGroupName();
        }
        SlaveEntity minSlave = slaveService.getSlaveByLoadAvg(slaveService.getAllSlave(userGroupName));
        String config = "{\"exec_local\":\"N\",\"exec_remote\":\"Y\",\"pass_export\":\"N\","
                + "\"exec_cluster\":\"N\",\"cluster_post\":\"Y\",\"cluster_prepare\":\"Y\",\"cluster_start\":\"Y\","
                + "\"cluster_show_trans\":\"N\",\"parameters\":[],"
                + "\"variables\":[{\"name\":\"Internal.Entry.Current.Directory\",\"value\":\"/\"},{\"name\":\"Internal.Job.Filename.Directory\",\"value\":\"Parent Job File Directory\"},{\"name\":\"Internal.Job.Filename.Name\",\"value\":\"Parent Job Filename\"},{\"name\":\"Internal.Job.Name\",\"value\":\"Parent Job Name\"},{\"name\":\"Internal.Job.Repository.Directory\",\"value\":\"Parent Job Repository Directory\"}],"
                + "\"arguments\":[],\"safe_mode\":\"N\",\"log_level\":\"Basic\",\"clear_log\":\"Y\",\"gather_metrics\":\"Y\",\"log_file\":\"N\",\"log_file_append\":\"N\",\"show_subcomponents\":\"Y\""
                + ",\"create_parent_folder\":\"N\",\"remote_server\":\"192.168.1.201\",\"replay_date\":\"\"}";
        JSONObject json = JSONObject.fromObject(config);
        json.put("remote_server", minSlave.getHostName());
        if (minSlave == null) {
            throw new Exception("当前无可用的正常节点!");
        } else {
            if (flag.equals("job")) {
                JobMeta jobMeta = RepositoryUtils.loadJobbyPath(path);
                JsonObject jsonObject = JsonObject.fromObject(json.toString());
                JobExecutionConfiguration jobExecutionConfiguration = JobExecutionConfigurationCodec.decode(jsonObject, jobMeta);
                JobExecutor jobExecutor = JobExecutor.initExecutor(jobExecutionConfiguration, jobMeta);
                Thread tr = new Thread(jobExecutor, "JobExecutor_" + jobExecutor.getExecutionId());
                tr.start();
            } else if (flag.equals("transformation")) {
                TransMeta transMeta = RepositoryUtils.loadTransByPath(path);
                JsonObject jsonObject = JsonObject.fromObject(json.toString());
                TransExecutionConfiguration transExecutionConfiguration = TransExecutionConfigurationCodec.decode(jsonObject, transMeta);
                TransExecutor transExecutor = TransExecutor.initExecutor(transExecutionConfiguration, transMeta);
                Thread tr = new Thread(transExecutor, "TransExecutor_" + transExecutor.getExecutionId());
                tr.start();
            }
            //输出结果返回给客户端
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(minSlave.getHostName());
            out.flush();
            out.close();
        }
    }

    /**
     * 定时执行作业-在时间执行作业之前
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/beforeFiexdExecute")
    protected void beforeFiexdExecute(HttpServletResponse response, HttpServletRequest request) throws Exception {
        boolean isSuccess = false;
        String json = "";
        try {
            isSuccess = jobService.beforeTimeExecuteJob(StringDateUtil.getMapByRequest(request), request);
            if (isSuccess) {
                json = "{'success':true,'isSuccess':true}";
            } else {
                json = "{'success':true,'isSuccess':false}";
            }
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 定时执行作业
     *
     * @param response
     * @param request
     * @param graphXml
     * @param executionConfiguration
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/fiexdExecute")
    protected void fiexdExecute(HttpServletResponse response, HttpServletRequest request, @RequestParam String graphXml, @RequestParam String executionConfiguration) throws Exception {
        try {
            jobService.addTimeExecuteJob(graphXml, executionConfiguration, request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取结构图信息
     *
     * @param taskName
     * @param type
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/detail")
    protected void detail(@RequestParam String taskName, @RequestParam String type) throws Exception {
        JsonObject jsonObject = new JsonObject();

        if (type.equals("trans")) {
            TransMeta transMeta = RepositoryUtils.loadTransByPath(taskName);
            jsonObject.put("GraphType", "TransGraph");
            GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.TRANS_CODEC);
            String graphXml = codec.encode(transMeta);
            jsonObject.put("graphXml", StringEscapeHelper.encode(graphXml));
        } else if (type.equals("job")) {
            JobMeta jobMeta = RepositoryUtils.loadJobbyPath(taskName);
            jsonObject.put("GraphType", "JobGraph");

            GraphCodec codec = (GraphCodec) PluginFactory.getBean(GraphCodec.JOB_CODEC);
            String graphXml = codec.encode(jobMeta);

            jsonObject.put("graphXml", StringEscapeHelper.encode(graphXml));
        }
        JsonUtils.response(jsonObject);
    }
}

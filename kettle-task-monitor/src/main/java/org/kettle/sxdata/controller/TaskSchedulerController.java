package org.kettle.sxdata.controller;

import net.sf.json.JSONObject;
import org.kettle.sxdata.bean.PageforBean;
import org.kettle.sxdata.entity.UserGroupAttributeEntity;
import org.kettle.sxdata.service.SchedulerService;
import org.kettle.sxdata.util.common.StringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @description: 作业调度控制器
 * @author: ZX
 * @date: 2018/11/20 11:57
 */
@Controller
@RequestMapping(value = "/scheduler")
public class TaskSchedulerController {
    @Autowired
    SchedulerService schedulerService;

    /**
     * 获取作业的定时调度信息
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getAllJobScheduler")
    @ResponseBody
    protected void getJobs(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取前台传递的分页参数
            int start = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("limit"));

            //获取查询参数
            Integer typeId = null;
            if (!StringDateUtil.isEmpty(request.getParameter("typeId"))) {
                if ("间隔重复".equals(request.getParameter("typeId"))) {
                    typeId = 1;
                } else if ("每天执行".equals(request.getParameter("typeId"))) {
                    typeId = 2;
                } else if ("每周执行".equals(request.getParameter("typeId"))) {
                    typeId = 3;
                } else {
                    typeId = 4;
                }
            }
            String hostName = request.getParameter("hostName");
            String jobName = request.getParameter("jobName");
            //获取当前用户所在的用户组
            UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
            String userGroupName = "";
            if (null != attr) {
                userGroupName = attr.getUserGroupName();
            }
            PageforBean bean = schedulerService.getAllSchedulerByPage(start, limit, typeId, hostName, jobName, userGroupName);
            //输出结果返回给客户端
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.fromObject(bean).toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取所有作业的定时调度信息
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/deleteScheduler")
    @ResponseBody
    protected void deleteScheduler(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            String[] taskIdArray = request.getParameterValues("taskIdArray");
            schedulerService.deleteScheduler(taskIdArray);
        } catch (org.quartz.SchedulerException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取修改前所需要的数据展现
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/beforeUpdateScheduler")
    @ResponseBody
    protected void beforeUpdateScheduler(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            //获取需要修改的定时作业详细信息
            String taskId = request.getParameter("taskId");
            JSONObject json = schedulerService.beforeUpdate(taskId);

            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(json.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 修改作业调度
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/updateJobScheduler")
    @ResponseBody
    protected void updateJobScheduler(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            String json = "";
            boolean isSuccess = schedulerService.updateSchedulerJob(StringDateUtil.getMapByRequest(request), request);
            if (isSuccess) {
                json = "{'success':true,'isSuccess':true}";
            } else {
                json = "{'success':true,'isSuccess':false}";
            }
            out.write(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}

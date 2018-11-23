package org.kettle.sxdata.controller;

import org.kettle.ext.utils.JsonObject;
import org.kettle.sxdata.entity.UserGroupAttributeEntity;
import org.kettle.sxdata.service.ControlService;
import org.kettle.sxdata.service.SchedulerService;
import org.kettle.sxdata.service.SlaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @description: 平台概况控制器
 * @author: ZX
 * @date: 2018/11/20 12:04
 */
@Controller
@RequestMapping(value = "/viewModule")
public class ViewController {
    @Autowired
    protected ControlService controlService;
    @Autowired
    protected SlaveService slaveService;
    @Autowired
    protected SchedulerService schedulerService;

    /**
     * 获取平台模块的数据
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getData")
    @ResponseBody
    protected void getDatabases(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            UserGroupAttributeEntity attr = (UserGroupAttributeEntity) request.getSession().getAttribute("userInfo");
            String userGroupName = "";
            if (null != attr) {
                userGroupName = attr.getUserGroupName();
            }
            Integer runningJobCount = controlService.getAllRunningJob(userGroupName).size();
            Integer runningTransCount = controlService.getAllRunningTrans(userGroupName).size();
            Integer slaveCount = slaveService.getAllSlaveSize();
            Integer schedulerCount = schedulerService.getSchedulerJobByLogin(userGroupName).size();

            JsonObject result = new JsonObject();

            JsonObject runningJob = new JsonObject();
            runningJob.put("value", runningJobCount);
            runningJob.put("name", "");
            JsonObject runningTrans = new JsonObject();
            runningTrans.put("value", runningTransCount);
            runningTrans.put("name", "");
            JsonObject slave = new JsonObject();
            slave.put("value", slaveCount);
            slave.put("name", "");
            JsonObject scheduler = new JsonObject();
            scheduler.put("value", schedulerCount);
            scheduler.put("name", "");

            result.put("runningJob", runningJob);
            result.put("runningTrans", runningTrans);
            result.put("slave", slave);
            result.put("scheduler", scheduler);


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
}

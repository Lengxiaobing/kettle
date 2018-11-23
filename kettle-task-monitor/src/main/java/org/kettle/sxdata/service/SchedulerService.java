package org.kettle.sxdata.service;

import net.sf.json.JSONObject;
import org.kettle.sxdata.bean.PageforBean;
import org.kettle.sxdata.entity.JobTimeSchedulerEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description: SchedulerService
 * @author: ZX
 * @date: 2018/11/20 17:23
 */
public interface SchedulerService {
    /**
     * 根据页面获取所有计划程序
     *
     * @param start
     * @param limit
     * @param typeId
     * @param hostName
     * @param jobName
     * @param userGroupName
     * @return
     * @throws Exception
     */
    PageforBean getAllSchedulerByPage(int start, int limit, Integer typeId, String hostName, String jobName, String userGroupName) throws Exception;

    /**
     * 删除计划程序
     *
     * @param taskIdArray
     * @throws Exception
     */
    void deleteScheduler(String[] taskIdArray) throws Exception;

    /**
     * 更新之前
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    JSONObject beforeUpdate(String taskId) throws Exception;

    /**
     * 修改计划程序作业
     *
     * @param params
     * @param request
     * @return
     * @throws Exception
     */
    boolean updateSchedulerJob(Map<String, Object> params, HttpServletRequest request) throws Exception;

    /**
     * 根据登录信息查询计划程序作业
     *
     * @param userGroupName
     * @return
     */
    List<JobTimeSchedulerEntity> getSchedulerJobByLogin(String userGroupName);

}

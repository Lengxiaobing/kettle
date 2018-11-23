package org.kettle.sxdata.service;

import net.sf.json.JSONObject;
import org.kettle.sxdata.entity.JobEntity;
import org.kettle.sxdata.entity.JobTimeSchedulerEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description: JobService
 * @author: ZX
 * @date: 2018/11/20 17:20
 */
public interface JobService {
    /**
     * 查询作业
     *
     * @param start
     * @param limit
     * @param name
     * @param createDate
     * @param userGroupName
     * @return
     * @throws Exception
     */
    JSONObject findJobs(int start, int limit, String name, String createDate, String userGroupName) throws Exception;

    /**
     * 删除作业
     *
     * @param jobPath
     * @param flag
     * @throws Exception
     */
    void deleteJobs(String jobPath, String flag) throws Exception;

    /**
     * 执行作业
     *
     * @param path
     * @param slaveId
     * @throws Exception
     */
    void executeJob(String path, Integer slaveId) throws Exception;

    /**
     * 判断工作是否相似
     *
     * @param willAddJobTimer
     * @return
     */
    boolean judgeJobIsAlike(JobTimeSchedulerEntity willAddJobTimer);

    /**
     * 添加时间执行作业
     *
     * @param graphXml
     * @param executionConfiguration
     * @param request
     * @throws Exception
     */
    void addTimeExecuteJob(String graphXml, String executionConfiguration, HttpServletRequest request) throws Exception;

    /**
     * 在时间执行作业之前
     *
     * @param params
     * @param request
     * @return
     * @throws Exception
     */
    boolean beforeTimeExecuteJob(Map<String, Object> params, HttpServletRequest request) throws Exception;

    /**
     * 得到所有计时器作业
     *
     * @return
     * @throws Exception
     */
    List<JobTimeSchedulerEntity> getAllTimerJob() throws Exception;

    /**
     * 根据id查询作业
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    JobEntity getJobById(Integer jobId) throws Exception;

    /**
     * 查询作业路径
     *
     * @param jobs
     * @return
     * @throws Exception
     */
    List<JobEntity> getJobPath(List<JobEntity> jobs) throws Exception;

    /**
     * 根据名称查询作业
     *
     * @param jobName
     * @return
     * @throws Exception
     */
    JobEntity getJobByName(String jobName) throws Exception;

    /**
     * 修改作业名称
     *
     * @param oldName
     * @param newName
     * @return
     */
    boolean updateJobName(String oldName, String newName);
}

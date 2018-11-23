package org.kettle.sxdata.dao;

import org.kettle.sxdata.entity.JobTimeSchedulerEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: JobSchedulerDao
 * @author: ZX
 * @date: 2018/11/20 15:41
 */
@Repository("JobSchedulerDao")
public interface JobSchedulerDao {
    /**
     * 查询所有计时器工作
     *
     * @param userGroupName
     * @return
     */
    List<JobTimeSchedulerEntity> getAllTimerJob(String userGroupName);

    /**
     * 添加计时器作业
     *
     * @param job
     */
    void addTimerJob(JobTimeSchedulerEntity job);

    /**
     * 查询总数
     *
     * @param typeId
     * @param slaves
     * @param jobName
     * @param userGroupName
     * @return
     */
    Integer getTotalCount(Integer typeId, String slaves, String jobName, String userGroupName);

    /**
     * 根据page查询计数器作业
     *
     * @param start
     * @param limit
     * @param typeId
     * @param slaves
     * @param jobName
     * @param userGroupName
     * @return
     */
    List<JobTimeSchedulerEntity> getTimerJobByPage(int start, int limit, Integer typeId, String slaves, String jobName, String userGroupName);

    /**
     * 删除计划程序
     *
     * @param taskId
     */
    void deleteScheduler(long taskId);

    /**
     * 根据TaskId查询Scheduler
     *
     * @param taskId
     * @return
     */
    JobTimeSchedulerEntity getSchedulerByTaskId(long taskId);

    /**
     * 更新计划程序
     *
     * @param schedulerJob
     */
    void updateScheduler(JobTimeSchedulerEntity schedulerJob);

    /**
     * 根据作业名称删除计划程序
     *
     * @param jobName
     */
    void deleteSchedulerByJobName(String jobName);

    /**
     * 根据作业名称获取计时器作业
     *
     * @param jobName
     * @return
     */
    List<JobTimeSchedulerEntity> getTimerJobByJobName(String jobName);

}

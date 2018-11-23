package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.JobEntity;

import java.util.List;

/**
 * @description: JobDao
 * @author: ZX
 * @date: 2018/11/20 15:25
 */
@Repository
public interface JobDao {
    /**
     * 得到这个页面的工作
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     */
    List<JobEntity> getThisPageJob(int start, int limit, String userGroupName);

    /**
     * 获得总记录数
     *
     * @param userGroupName
     * @return
     */
    Integer getTotalCount(String userGroupName);

    /**
     * 带条件的查询
     *
     * @param start
     * @param limit
     * @param namme
     * @param date
     * @param userGroupName
     * @return
     */
    List<JobEntity> conditionFindJobs(int start, int limit, String namme, String date, String userGroupName);

    /**
     * 根据条件查询总记录数
     *
     * @param name
     * @param date
     * @param UserGroupName
     * @return
     */
    Integer conditionFindJobCount(String name, String date, String UserGroupName);

    /**
     * 根据id查询job
     *
     * @param jobId
     * @return
     */
    JobEntity getJobById(Integer jobId);

    /**
     * 根据name查询job
     *
     * @param jobName
     * @return
     */
    JobEntity getJobByName(String jobName);

    /**
     * 更新作业的作业名称
     *
     * @param oldName
     * @param newName
     */
    void updateJobNameforJob(String oldName, String newName);
}

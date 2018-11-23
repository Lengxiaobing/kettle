package org.kettle.sxdata.dao;

import org.kettle.ext.task.ExecutionTraceEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: ExecutionTraceDao
 * @author: ZX
 * @date: 2018/11/20 15:20
 */
@Repository("taskExecutionTraceDao")
public interface ExecutionTraceDao {
    /**
     * 添加执行跟踪
     *
     * @param trace
     */
    void addExecutionTrace(ExecutionTraceEntity trace);

    /**
     * 获取所有按页面记录
     *
     * @param start
     * @param limit
     * @param statu
     * @param type
     * @param startDate
     * @param taskName
     * @param userGroupName
     * @return
     */
    List<ExecutionTraceEntity> getAllLogByPage(int start, int limit, String statu, String type, String startDate, String taskName, String userGroupName);

    /**
     * 获取所有日志计数
     *
     * @param statu
     * @param type
     * @param startDate
     * @param taskName
     * @param userGroupName
     * @return
     */
    Integer getAllLogCount(String statu, String type, String startDate, String taskName, String userGroupName);

    /**
     * 获取跟踪ID
     *
     * @param id
     * @return
     */
    ExecutionTraceEntity getTraceById(Integer id);
}

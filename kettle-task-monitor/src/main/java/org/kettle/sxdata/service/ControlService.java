package org.kettle.sxdata.service;

import org.kettle.sxdata.entity.TaskControlEntity;
import org.pentaho.di.trans.step.StepStatus;

import java.util.List;

/**
 * @description: ControlService
 * @author: ZX
 * @date: 2018/11/20 17:14
 */
public interface ControlService {
    /**
     * 查询所有运行的作业
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<TaskControlEntity> getAllRunningJob(String userGroupName) throws Exception;

    /**
     * 查询所有运行转换
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<TaskControlEntity> getAllRunningTrans(String userGroupName) throws Exception;

    /**
     * 获取作业的日志详细信息
     *
     * @param id
     * @param hostName
     * @return
     * @throws Exception
     */
    String getLogDetailForJob(String id, String hostName) throws Exception;

    /**
     * 获取转换的日志详细信息
     *
     * @param id
     * @param hostName
     * @return
     * @throws Exception
     */
    String getLogDetailForTrans(String id, String hostName) throws Exception;

    /**
     * 获取转换详情
     *
     * @param id
     * @param hostName
     * @return
     * @throws Exception
     */
    List<StepStatus> getTransDetail(String id, String hostName) throws Exception;

    /**
     * 停止转换
     *
     * @param hostName
     * @param id
     * @throws Exception
     */
    void stopTrans(String hostName, String id) throws Exception;

    /**
     * 停止作业
     *
     * @param hostName
     * @param id
     * @throws Exception
     */
    void stopJob(String hostName, String id) throws Exception;

    /**
     * 暂停或开始转换
     *
     * @param id
     * @param hostName
     * @throws Exception
     */
    void pauseOrStartTrans(String[] id, String[] hostName) throws Exception;
}

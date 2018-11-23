package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.JobEntity;
import org.kettle.sxdata.entity.TaskGroupAttributeEntity;
import org.kettle.sxdata.entity.TaskGroupEntity;
import org.kettle.sxdata.entity.TransformationEntity;

import java.util.List;

/**
 * @description: TaskGroupDao
 * @author: ZX
 * @date: 2018/11/20 15:57
 */
@Repository("taskTaskGroupDao")
public interface TaskGroupDao {
    /**
     * 获取所有任务组
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @param taskGroupName
     * @param createDate
     * @return
     */
    List<TaskGroupEntity> getAllTaskGroup(int start, int limit, String userGroupName, String taskGroupName, String createDate);

    /**
     * 获得任务组总数
     *
     * @param userGroupName
     * @return
     */
    Integer getTotalCountTaskGroup(String userGroupName);

    /**
     * 添加任务组
     *
     * @param taskGroup
     */
    void addTaskGroup(TaskGroupEntity taskGroup);

    /**
     * 添加任务组属性
     *
     * @param taskGroupAttribute
     */
    void addTaskGroupAttribute(TaskGroupAttributeEntity taskGroupAttribute);

    /**
     * 查询所有的job
     *
     * @param userGroupName
     * @return
     */
    List<JobEntity> getAllJob(String userGroupName);

    /**
     * 查询所有Trans
     *
     * @param userGroupName
     * @return
     */
    List<TransformationEntity> getAllTrans(String userGroupName);

    /**
     * 查询所有任务组无限制
     *
     * @return
     */
    List<TaskGroupEntity> getAllTaskGroupNoLimit();

    /**
     * 更新任务组
     *
     * @param taskGroup
     */
    void updateTaskGroup(TaskGroupEntity taskGroup);

    /**
     * 更新任务组属性
     *
     * @param oldName
     * @param newName
     */
    void updateTaskGroupAttributes(String oldName, String newName);

    /**
     * 更新任务名称，属性
     *
     * @param oldName
     * @param newName
     * @param type
     * @param dirName
     */
    void updateTaskNameforAttr(String oldName, String newName, String type, String dirName);

    /**
     * 根据名称查询任务组属性
     *
     * @param name
     * @return
     */
    List<TaskGroupAttributeEntity> getTaskGroupAttributesByName(String name);

    /**
     * 根据名称删除任务组属性
     *
     * @param name
     */
    void deleteTaskGroupAttributesByName(String name);

    /**
     * 根据名称删除任务组
     *
     * @param name
     */
    void deleteTaskGroupByName(String name);

    /**
     * 是否包含任务
     *
     * @param taskName
     * @param type
     * @param groupName
     * @return
     */
    Integer isContainsTask(String taskName, String type, String groupName);

    /**
     * 根据任务名称删除任务组属性
     *
     * @param taskName
     * @param type
     */
    void deleteTaskGroupAttributesByTaskName(String taskName, String type);

    /**
     * 根据任务名称获取任务组
     *
     * @param taskName
     * @param type
     * @return
     */
    List<TaskGroupAttributeEntity> getTaskGroupByTaskName(String taskName, String type);

    /**
     * 根据ID获取任务组
     *
     * @param id
     * @return
     */
    TaskGroupEntity getTaskGroupById(Integer id);

    /**
     * 修改用户组-任务组关系表中的任务组名
     *
     * @param oldName
     * @param newName
     */
    void updateTaskGroupForTaskRelation(String oldName, String newName);

    /**
     * 删除用户组-任务组关系表中的某个任务组记录
     *
     * @param taskGroupName
     */
    void deleteUserTaskRelationByName(String taskGroupName);

    /**
     * 查询当前用户组下的所有任务组
     *
     * @param userGroupName
     * @return
     */
    List<TaskGroupEntity> getTaskGroupByThisUser(String userGroupName);
}

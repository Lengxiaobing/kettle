package org.kettle.sxdata.service;

import org.kettle.sxdata.entity.TaskGroupAttributeEntity;
import org.kettle.sxdata.entity.TaskGroupEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: TaskGroupService
 * @author: ZX
 * @date: 2018/11/20 17:32
 */
public interface TaskGroupService {
    /**
     * 获取当前登录用户的所有任务组信息，分页形式，用户模块暂无
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @param taskGroupName
     * @param createDate
     * @return
     * @throws Exception
     */
    String getAllTaskGroupByLogin(int start, int limit, String userGroupName, String taskGroupName, String createDate) throws Exception;

    /**
     * 添加任务组
     *
     * @param request
     * @throws Exception
     */
    void addTaskGroup(HttpServletRequest request) throws Exception;

    /**
     * 添加前获取所有任务
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String getAllTaskBeforeAdd(String userGroupName) throws Exception;

    /**
     * 判断群组名称是否存在
     *
     * @param name
     * @return
     * @throws Exception
     */
    boolean decideGroupNameExist(String name) throws Exception;

    /**
     * 修改任务组
     *
     * @param taskGroup
     * @throws Exception
     */
    void updateTaskGroup(TaskGroupEntity taskGroup) throws Exception;

    /**
     * 根据名称查询任务组属性
     *
     * @param name
     * @return
     * @throws Exception
     */
    String selectTaskGroupAttributesByName(String name) throws Exception;

    /**
     * 删除任务组和属性
     *
     * @param name
     * @throws Exception
     */
    void deleteTaskGroupAndAttributes(String name) throws Exception;

    /**
     * 是否包含任务
     *
     * @param taskName
     * @param type
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<TaskGroupEntity> isContainsTask(String taskName, String type, String userGroupName) throws Exception;

    /**
     * 分配任务组
     *
     * @param items
     * @param taskName
     * @param type
     * @throws Exception
     */
    void assignedTaskGroup(List<TaskGroupAttributeEntity> items, String taskName, String type) throws Exception;

    /**
     * 查询所有没有页面的任务组
     *
     * @return
     * @throws Exception
     */
    String getAllTaskGroupNoPage() throws Exception;

    /**
     * 添加前的所有任务组
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<TaskGroupEntity> AllTaskGroupBeforeAdd(String userGroupName) throws Exception;
}

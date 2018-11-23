package org.kettle.sxdata.service;

import org.kettle.sxdata.entity.UserGroupEntity;

import java.util.List;

/**
 * @description: UserGroupService
 * @author: ZX
 * @date: 2018/11/20 17:37
 */
public interface UserGroupService {
    /**
     * 根据页面获取用户组
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String getUserGroupByPage(int start, int limit, String userGroupName) throws Exception;

    /**
     * 判断群组名称是否存在
     *
     * @param name
     * @return
     * @throws Exception
     */
    String decideGroupNameExist(String name) throws Exception;

    /**
     * 添加用户组
     *
     * @param taskGroupNameArray
     * @param slaveIdArray
     * @param userGroupName
     * @param userGroupDesc
     * @throws Exception
     */
    void addUserGroup(String[] taskGroupNameArray, String[] slaveIdArray, String userGroupName, String userGroupDesc) throws Exception;

    /**
     * 在分配任务组之前
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String[] beforeAssignedTaskGroup(String userGroupName) throws Exception;

    /**
     * 在分配从属之前
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String[] beforeAssignedSlave(String userGroupName) throws Exception;

    /**
     * 分配从属
     *
     * @param slaveIdArray
     * @param userGroupName
     * @throws Exception
     */
    void assignedSlave(String[] slaveIdArray, String userGroupName) throws Exception;

    /**
     * 分配从属组
     *
     * @param taskGroupNameArray
     * @param userGroupName
     * @throws Exception
     */
    void assignedTaskGroup(String[] taskGroupNameArray, String userGroupName) throws Exception;

    /**
     * 更新用户组
     *
     * @param userGroupId
     * @param userGroupName
     * @param userGroupDesc
     * @return
     * @throws Exception
     */
    String updateUserGroup(Integer userGroupId, String userGroupName, String userGroupDesc) throws Exception;

    /**
     * 删除用户组
     *
     * @param userGroupName
     * @throws Exception
     */
    void deleteUserGroup(String userGroupName) throws Exception;

    /**
     * 获取所有用户组
     *
     * @return
     * @throws Exception
     */
    List<UserGroupEntity> getAllUserGroup() throws Exception;

}

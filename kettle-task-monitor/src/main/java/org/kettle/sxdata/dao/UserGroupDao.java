package org.kettle.sxdata.dao;

import org.kettle.sxdata.entity.SlaveUserRelationEntity;
import org.kettle.sxdata.entity.TaskUserRelationEntity;
import org.kettle.sxdata.entity.UserGroupAttributeEntity;
import org.kettle.sxdata.entity.UserGroupEntity;
import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.*;

import java.util.List;

/**
 * @description: UserGroupDao
 * @author: ZX
 * @date: 2018/11/20 16:05
 */
@Repository
public interface UserGroupDao {
    /**
     * 分页形式查询用户组
     *
     * @param start
     * @param limit
     * @param name
     * @return
     */
    List<UserGroupEntity> selectUserGroupByPage(int start, int limit, String name);

    /**
     * 查询所有用户组
     *
     * @return
     */
    List<UserGroupEntity> allUserGroup();

    /**
     * 查询用户组的总数量
     *
     * @param name
     * @return
     */
    Integer userGroupConut(String name);

    /**
     * 根据用户组名查询当前用户组下的所有用户
     *
     * @param name
     * @return
     */
    List<UserGroupAttributeEntity> getUsersByUserGroupName(String name);

    /**
     * 根据用名查询当前用户的权限相关信息
     *
     * @param name
     * @return
     */
    UserGroupAttributeEntity getInfoByUserName(String name);

    /**
     * 根据用户组名获取当前用户可见的任务组
     *
     * @param name
     * @return
     */
    List<TaskUserRelationEntity> getTaskGroupsByUserGroupName(String name);

    /**
     * 根据用户组名获取当前用户可见的节点
     *
     * @param name
     * @return
     */
    List<SlaveUserRelationEntity> getSlavesByUserGroupName(String name);

    /**
     * 添加用户组
     *
     * @param userGroup
     */
    void addUserGroup(UserGroupEntity userGroup);

    /**
     * 添加用户组成员表(即添加该用户组下有哪些用户)
     *
     * @param attributes
     */
    void addUserGroupAttribute(UserGroupAttributeEntity attributes);

    /**
     * 添加用户组可见的节点记录
     *
     * @param entry
     */
    void addUserSlaveRelation(SlaveUserRelationEntity entry);

    /**
     * 添加用户组可见的任务组记录
     *
     * @param entry
     */
    void addTaskUserRelation(TaskUserRelationEntity entry);

    /**
     * 修改用户组
     *
     * @param userGroup
     */
    void updateUserGroup(UserGroupEntity userGroup);

    /**
     * 删除用户组
     *
     * @param name
     */
    void deleteUserGroupByName(String name);

    /**
     * 删除用户组下的所有用户
     *
     * @param name
     */
    void deleteUserAttributeByGroupName(String name);

    /**
     * 删除用户组下可见的节点记录
     *
     * @param name
     */
    void deleteSlaveRelationByGroupName(String name);

    /**
     * 删除用户组下可见的任务组记录
     *
     * @param name
     */
    void deleteTaskRelationByGroupName(String name);

    /**
     * 根据用户组名或者用户id查找用户组
     *
     * @param name
     * @param id
     * @return
     */
    UserGroupEntity selectUserGroupByNameOrId(String name, Integer id);

    /**
     * 修改用户组在其它关系表中的用户组名
     *
     * @param tableName
     * @param oldName
     * @param newName
     */
    void updateUserGroupNameForRelation(String tableName, String oldName, String newName);

    /**
     * 根据用户名修改用户组与用户的关联表
     *
     * @param attr
     */
    void updateUserGroupAttrByName(UserGroupAttributeEntity attr);

    /**
     * 根据用户名删除用户用户成员表中的记录
     *
     * @param username
     */
    void deleteUserAttributeByName(String username);
}

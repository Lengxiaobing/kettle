package org.kettle.sxdata.service.impl;

import net.sf.json.JSONObject;
import org.kettle.sxdata.bean.PageforBean;
import org.kettle.sxdata.dao.UserGroupDao;
import org.kettle.sxdata.entity.SlaveUserRelationEntity;
import org.kettle.sxdata.entity.TaskUserRelationEntity;
import org.kettle.sxdata.entity.UserGroupEntity;
import org.kettle.sxdata.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: UserGroupServiceImpl
 * @author: ZX
 * @date: 2018/11/20 17:07
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Autowired
    private UserGroupDao userGroupDao;

    /**
     * 分页形式获取用户组
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     * @throws Exception
     */
    @Override
    public String getUserGroupByPage(int start, int limit, String userGroupName) throws Exception {
        PageforBean page = new PageforBean();
        List<UserGroupEntity> groups = userGroupDao.selectUserGroupByPage(start, limit, userGroupName);
        Integer count = userGroupDao.userGroupConut(userGroupName);
        page.setRoot(groups);
        page.setTotalProperty(count);
        return JSONObject.fromObject(page).toString();
    }

    /**
     * 判断传递用户组名是否已存在
     *
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public String decideGroupNameExist(String name) throws Exception {
        String result = "N";
        UserGroupEntity items = userGroupDao.selectUserGroupByNameOrId(name, null);
        if (null != items) {
            result = "Y";
        }
        return result;
    }

    /**
     * 新增用户组(以及关联表)
     *
     * @param taskGroupNameArray
     * @param slaveIdArray
     * @param userGroupName
     * @param userGroupDesc
     * @throws Exception
     */
    @Override
    public void addUserGroup(String[] taskGroupNameArray, String[] slaveIdArray, String userGroupName, String userGroupDesc) throws Exception {
        //添加用户组
        UserGroupEntity userGroup = new UserGroupEntity();
        userGroup.setUserGroupDesc(userGroupDesc);
        userGroup.setUserGroupName(userGroupName);
        userGroupDao.addUserGroup(userGroup);
        //添加用户组下的可见任务组记录
        if (null != taskGroupNameArray && taskGroupNameArray.length > 0) {
            for (String taskGroupName : taskGroupNameArray) {
                if (taskGroupName.equals("")) {
                    continue;
                }
                TaskUserRelationEntity taskUser = new TaskUserRelationEntity();
                taskUser.setUserGroupName(userGroupName);
                taskUser.setTaskGroupName(taskGroupName);
                userGroupDao.addTaskUserRelation(taskUser);
            }
        }
        //添加用户组下的可见节点记录
        if (null != slaveIdArray && slaveIdArray.length > 0) {
            for (String slaveId : slaveIdArray) {
                if (slaveId.equals("")) {
                    continue;
                }
                SlaveUserRelationEntity slaveUser = new SlaveUserRelationEntity();
                slaveUser.setUserGroupName(userGroupName);
                slaveUser.setSlaveId(Integer.valueOf(slaveId));
                userGroupDao.addUserSlaveRelation(slaveUser);
            }
        }
    }

    /**
     * 分配任务组前 先获取该用户组下可见的任务组
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    @Override
    public String[] beforeAssignedTaskGroup(String userGroupName) throws Exception {
        String[] taskGroupNameArray = null;
        List<TaskUserRelationEntity> items = userGroupDao.getTaskGroupsByUserGroupName(userGroupName);
        if (null != items && items.size() > 0) {
            taskGroupNameArray = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                taskGroupNameArray[i] = items.get(i).getTaskGroupName().toString();
            }
        } else {
            taskGroupNameArray = new String[0];
        }
        return taskGroupNameArray;
    }

    /**
     * 分配节点前 先获取该用户组下可见的节点
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    @Override
    public String[] beforeAssignedSlave(String userGroupName) throws Exception {
        String[] slaveIdArray = null;
        List<SlaveUserRelationEntity> items = userGroupDao.getSlavesByUserGroupName(userGroupName);
        if (null != items && items.size() > 0) {
            slaveIdArray = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                slaveIdArray[i] = items.get(i).getSlaveId().toString();
            }
        } else {
            slaveIdArray = new String[0];
        }

        return slaveIdArray;
    }

    /**
     * 为用户组分配可见节点
     *
     * @param slaveIdArray
     * @param userGroupName
     * @throws Exception
     */
    @Override
    public void assignedSlave(String[] slaveIdArray, String userGroupName) throws Exception {
        userGroupDao.deleteSlaveRelationByGroupName(userGroupName);
        if (null != slaveIdArray && slaveIdArray.length > 0) {
            for (String slaveId : slaveIdArray) {
                if (slaveId.equals("")) {
                    continue;
                }
                SlaveUserRelationEntity slaveUser = new SlaveUserRelationEntity();
                slaveUser.setSlaveId(Integer.valueOf(slaveId));
                slaveUser.setUserGroupName(userGroupName);
                userGroupDao.addUserSlaveRelation(slaveUser);
            }
        }
    }

    /**
     * 为用户组分配可见任务组
     *
     * @param taskGroupNameArray
     * @param userGroupName
     * @throws Exception
     */
    @Override
    public void assignedTaskGroup(String[] taskGroupNameArray, String userGroupName) throws Exception {
        userGroupDao.deleteTaskRelationByGroupName(userGroupName);
        if (null != taskGroupNameArray && taskGroupNameArray.length > 0) {
            for (String taskGroupName : taskGroupNameArray) {
                if (taskGroupName.equals("")) {
                    continue;
                }
                TaskUserRelationEntity taskUser = new TaskUserRelationEntity();
                taskUser.setTaskGroupName(taskGroupName);
                taskUser.setUserGroupName(userGroupName);
                userGroupDao.addTaskUserRelation(taskUser);
            }
        }
    }

    @Override
    public String updateUserGroup(Integer userGroupId, String userGroupName, String userGroupDesc) throws Exception {
        String result = "N";
        UserGroupEntity items = userGroupDao.selectUserGroupByNameOrId(userGroupName, null);
        //判断该用户组名是否已存在
        if (null != items && items.getUserGroupId() != userGroupId) {
            result = "Y";
        }
        if (result.equals("N")) {
            UserGroupEntity oldUserGroup = userGroupDao.selectUserGroupByNameOrId("", userGroupId);
            //组装对象
            UserGroupEntity userGroup = new UserGroupEntity();
            userGroup.setUserGroupName(userGroupName);
            userGroup.setUserGroupDesc(userGroupDesc);
            userGroup.setUserGroupId(userGroupId);
            //修改用户组表
            userGroupDao.updateUserGroup(userGroup);
            //修改和用户组关联表中的用户组名
            if (!oldUserGroup.getUserGroupName().equals(userGroupName)) {
                userGroupDao.updateUserGroupNameForRelation("d_user_group_member_attribute", oldUserGroup.getUserGroupName(), userGroupName);
                userGroupDao.updateUserGroupNameForRelation("d_user_group_slaves_relation", oldUserGroup.getUserGroupName(), userGroupName);
                userGroupDao.updateUserGroupNameForRelation("d_user_group_tasks_relation", oldUserGroup.getUserGroupName(), userGroupName);
            }
        }
        return result;
    }

    @Override
    public void deleteUserGroup(String userGroupName) throws Exception {
        userGroupDao.deleteTaskRelationByGroupName(userGroupName);
        userGroupDao.deleteSlaveRelationByGroupName(userGroupName);
        userGroupDao.deleteUserAttributeByGroupName(userGroupName);
        userGroupDao.deleteUserGroupByName(userGroupName);
    }

    @Override
    public List<UserGroupEntity> getAllUserGroup() throws Exception {
        return userGroupDao.allUserGroup();
    }


}

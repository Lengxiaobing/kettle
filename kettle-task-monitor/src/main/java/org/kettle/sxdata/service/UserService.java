package org.kettle.sxdata.service;

import org.kettle.sxdata.entity.UserEntity;
import org.kettle.sxdata.entity.UserGroupAttributeEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: UserService
 * @author: ZX
 * @date: 2018/11/20 17:37
 */
public interface UserService {
    /**
     * 删除用户
     *
     * @param id
     * @param username
     * @throws Exception
     */
    void deleteUser(Integer id, String username) throws Exception;

    /**
     * 更新用户
     *
     * @param user
     * @param attr
     * @throws Exception
     */
    void updateUser(UserEntity user, UserGroupAttributeEntity attr) throws Exception;

    /**
     * 添加用户
     *
     * @param user
     * @param attribute
     * @return
     * @throws Exception
     */
    boolean addUser(UserEntity user, UserGroupAttributeEntity attribute) throws Exception;

    /**
     * 获取一些用户
     *
     * @param start
     * @param limit
     * @param request
     * @return
     * @throws Exception
     */
    String getUsersLimit(int start, int limit, HttpServletRequest request) throws Exception;

    /**
     * 更具名称获取用户
     *
     * @param login
     * @return
     * @throws Exception
     */
    List<UserEntity> getUserByName(String login) throws Exception;

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param request
     * @return
     * @throws Exception
     */
    String login(String userName, String password, HttpServletRequest request) throws Exception;

    /**
     * 分配用户组
     *
     * @param attr
     * @throws Exception
     */
    void allotUserGroup(UserGroupAttributeEntity attr) throws Exception;

    /**
     * 根据用户组查询用户
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<UserEntity> getUsers(String userGroupName) throws Exception;

    /**
     * 修改密码
     *
     * @param user
     * @throws Exception
     */
    void updatePassword(UserEntity user) throws Exception;
}

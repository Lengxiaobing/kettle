package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.UserEntity;

import java.util.List;

/**
 * @description: UserDao
 * @author: ZX
 * @date: 2018/11/20 16:04
 */
@Repository
public interface UserDao {

    /**
     * 根据name获取user
     *
     * @param name
     * @return
     */
    List<UserEntity> getUserbyName(String name);

    /**
     * 根据条件查询Users
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @param username
     * @param userType
     * @return
     */
    List<UserEntity> getUsersLimit(int start, int limit, String userGroupName, String username, Integer userType);

    /**
     * 更新User
     *
     * @param user
     */
    void updateUser(UserEntity user);

    /**
     * 删除User
     *
     * @param userId
     */
    void deleteUser(Integer userId);

    /**
     * 添加user
     *
     * @param user
     */
    void addUser(UserEntity user);

    /**
     * 根据userGroupName查询user总数
     *
     * @param userGroupName
     * @return
     */
    Integer getUserCount(String userGroupName);

    /**
     * 查询所有user
     *
     * @return
     */
    List<UserEntity> getAllUsers();

    /**
     * 查询MaxId
     *
     * @return
     */
    Integer selectMaxId();

    /**
     * 根据userGroup查询Users
     *
     * @param userGroupName
     * @return
     */
    List<UserEntity> getUsers(String userGroupName);

}

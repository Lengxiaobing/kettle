package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.SlaveEntity;

import java.util.List;

/**
 * @description: SlaveDao
 * @author: ZX
 * @date: 2018/11/20 15:54
 */
@Repository
public interface SlaveDao {
    /**
     * 得到Slave总数
     *
     * @return
     */
    Integer getSlaveTotalCount();

    /**
     * 根据id查询Slave
     *
     * @param id
     * @return
     */
    SlaveEntity getSlaveById(Integer id);

    /**
     * 获取所有Slave
     *
     * @param userGroupName
     * @return
     */
    List<SlaveEntity> getAllSlave(String userGroupName);

    /**
     * 根据hostName查询Slave
     *
     * @param hostName
     * @return
     */
    SlaveEntity getSlaveByHostName(String hostName);

    /**
     * 根据hostName和port查询Slave
     *
     * @param hostName
     * @param port
     * @return
     */
    SlaveEntity getSlaveByHostNameAndPort(String hostName, String port);

    /**
     * 根据hostName、port和name查询slave
     *
     * @param hostName
     * @param port
     * @param name
     * @return
     */
    List<SlaveEntity> getSlaveByHostNameAndPortOrName(String hostName, String port, String name);


    /**
     * 根据pageInfo查询Slave
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     */
    List<SlaveEntity> findSlaveByPageInfo(Integer start, Integer limit, String userGroupName);

    /**
     * 添加Slave
     *
     * @param slave
     */
    void addSlave(SlaveEntity slave);

    /**
     * 查询MaxId
     *
     * @return
     */
    Integer selectMaxId();

    /**
     * 删除TransSlave
     *
     * @param slaveId
     */
    void deleteTransSlave(Integer slaveId);

    /**
     * 删除从属用户组
     *
     * @param slaveId
     */
    void deleteSlaveUserGroup(Integer slaveId);

    /**
     * 删除Slave Server
     *
     * @param slaveId
     */
    void deleteSlaveServer(Integer slaveId);

    /**
     * 更新Slave Server
     *
     * @param slave
     */
    void updateSlaveServer(SlaveEntity slave);
}

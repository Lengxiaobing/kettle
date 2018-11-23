package org.kettle.sxdata.service;

import org.kettle.sxdata.bean.PageforBean;
import org.kettle.sxdata.entity.SlaveEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: SlaveService
 * @author: ZX
 * @date: 2018/11/20 17:26
 */
public interface SlaveService {

    /**
     * 获取所有从属数量
     *
     * @return
     */
    Integer getAllSlaveSize();

    /**
     * 获取所有从属
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    List<SlaveEntity> getAllSlave(String userGroupName) throws Exception;

    /**
     * 通过负载均衡获取从属
     *
     * @param slaves
     * @return
     * @throws Exception
     */
    SlaveEntity getSlaveByLoadAvg(List<SlaveEntity> slaves) throws Exception;

    /**
     * 根据页面信息获取从属
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     * @throws Exception
     */
    PageforBean findSlaveByPageInfo(Integer start, Integer limit, String userGroupName) throws Exception;

    /**
     * 删除从属
     *
     * @param slaveId
     * @throws Exception
     */
    void deleteSlave(Integer slaveId) throws Exception;

    /**
     * 从属测试
     *
     * @param hostName
     * @return
     * @throws Exception
     */
    String slaveTest(String hostName) throws Exception;

    /**
     * 所有从属配额
     *
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String allSlaveQuato(String userGroupName) throws Exception;

    /**
     * 根据条件查询从属配额
     *
     * @param quatoType
     * @param viewType
     * @param maxOrAvg
     * @param chooseDate
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String slaveQuatoByCondition(String quatoType, String viewType, String maxOrAvg, String chooseDate, String userGroupName) throws Exception;

    /**
     * 从属陪我折线图
     *
     * @param quatoType
     * @param maxOrAvg
     * @param chooseDate
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String slaveQuatoLineChart(String quatoType, String maxOrAvg, String chooseDate, String userGroupName) throws Exception;

    /**
     * 从属配额柱状图
     *
     * @param quatoType
     * @param maxOrAvg
     * @param chooseDate
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String slaveQuatoColumnDiagram(String quatoType, String maxOrAvg, String chooseDate, String userGroupName) throws Exception;

    /**
     * 从属配额 HTML文本
     *
     * @param quatoType
     * @param maxOrAvg
     * @param chooseDate
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String slaveQuatoHTMLText(String quatoType, String maxOrAvg, String chooseDate, String userGroupName) throws Exception;

    /**
     * 添加从属
     *
     * @param request
     * @return
     * @throws Exception
     */
    String addSlave(HttpServletRequest request) throws Exception;

    /**
     * 通过主机名获取从属
     *
     * @param id
     * @return
     * @throws Exception
     */
    SlaveEntity getSlaveByHostName(Integer id) throws Exception;

    /**
     * 修改从属
     *
     * @param request
     * @return
     * @throws Exception
     */
    String updateSlave(HttpServletRequest request) throws Exception;
}

package org.kettle.sxdata.dao;

import org.kettle.sxdata.entity.SlaveEntity;
import org.pentaho.big.data.impl.cluster.NamedClusterImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description: HadoopClusterDao
 * @author: ZX
 * @date: 2018/11/20 15:22
 */
@Repository("taskHadoopClusterDao")
public interface HadoopClusterDao {
    /**
     * 添加群集
     *
     * @param attrId
     * @param elementId
     * @param attrParId
     * @param attrKey
     * @param attrValue
     */
    void addCluster(Integer attrId, Integer elementId, Integer attrParId, String attrKey, String attrValue);

    /**
     * 查询所有群集
     *
     * @return
     */
    List<NamedClusterImpl> allCluster();

    /**
     * 按名称获取群集
     *
     * @param clusterName
     * @return
     */
    NamedClusterImpl getClusterByName(String clusterName);

    /**
     * 得到集群
     *
     * @return
     */
    List<SlaveEntity> getClusters();

    /**
     * 添加Cluster 元素
     *
     * @param map
     */
    void addClusterEle(Map<String, Object> map);

    /**
     * 得到下一个ID
     *
     * @param tableName
     * @param field
     * @return
     */
    Integer getNextId(String tableName, String field);

    /**
     * 删除元素
     *
     * @param elementId
     */
    void deleteEle(Integer elementId);

    /**
     * 删除元素属性
     *
     * @param elementId
     */
    void deleteEleAttr(Integer elementId);

    /**
     * 通过群集名称获取元素Id
     *
     * @param clusterName
     * @return
     */
    Integer getEleIdByClusterName(String clusterName);

    /**
     * 更新元素
     *
     * @param element
     */
    void updateEle(Map<String, Object> element);

    /**
     * 更新元素属性
     *
     * @param eleAttr
     */
    void updateEleAttr(Map<String, Object> eleAttr);
}

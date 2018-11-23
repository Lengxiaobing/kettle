package org.kettle.sxdata.service;

import org.pentaho.big.data.impl.cluster.NamedClusterImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: HadoopService
 * @author: ZX
 * @date: 2018/11/20 17:17
 */
public interface HadoopService {
    /**
     * 添加Hadoop集群
     *
     * @param cluster
     * @return
     * @throws Exception
     */
    String addHadoopCluster(NamedClusterImpl cluster) throws Exception;

    /**
     * 获取所有群集
     *
     * @return
     */
    List<NamedClusterImpl> getAllCluster();

    /**
     * 获得群集
     *
     * @param name
     * @return
     */
    NamedClusterImpl getCluster(String name);

    /**
     * 删除群集
     *
     * @param clusterName
     */
    void deleteCluster(String clusterName);

    /**
     * 通过群集名称获取元素id
     *
     * @param clusterName
     * @return
     */
    Integer getEleIdByClusterName(String clusterName);

    /**
     * 更新Hadoop集群
     *
     * @param request
     * @return
     * @throws Exception
     */
    String updateHadoopCluster(HttpServletRequest request) throws Exception;
}

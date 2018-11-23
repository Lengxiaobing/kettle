package org.kettle.sxdata.service;

import net.sf.json.JSONObject;
import org.kettle.sxdata.entity.TransformationEntity;

import java.util.List;

/**
 * @description: TransService
 * @author: ZX
 * @date: 2018/11/20 17:37
 */
public interface TransService {
    /**
     * 查询转换
     *
     * @param start
     * @param limit
     * @param namme
     * @param date
     * @param userGroupName
     * @return
     * @throws Exception
     */
    JSONObject findTrans(int start, int limit, String namme, String date, String userGroupName) throws Exception;

    /**
     * 删除转换
     *
     * @param transPath
     * @param flag
     * @throws Exception
     */
    void deleteTransformation(String transPath, String flag) throws Exception;

    /**
     * 执行转换
     *
     * @param path
     * @param slaveId
     * @throws Exception
     */
    void executeTransformation(String path, Integer slaveId) throws Exception;

    /**
     * 获取转换路径
     *
     * @param items
     * @return
     * @throws Exception
     */
    List<TransformationEntity> getTransPath(List<TransformationEntity> items) throws Exception;

    /**
     * 根据名称出查询转换
     *
     * @param transName
     * @return
     * @throws Exception
     */
    TransformationEntity getTransByName(String transName) throws Exception;

    /**
     * 修改转换名称
     *
     * @param oldName
     * @param newName
     * @return
     */
    boolean updateTransName(String oldName, String newName);
}

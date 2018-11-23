package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.TransformationEntity;

import java.util.List;

/**
 * @description: TransDao
 * @author: ZX
 * @date: 2018/11/20 16:04
 */
@Repository
public interface TransDao {
    /**
     * 获取当页的记录
     *
     * @param start
     * @param limit
     * @param userGroupName
     * @return
     */
    List<TransformationEntity> getThisPageTrans(int start, int limit, String userGroupName);

    /**
     * 获取总记录数
     *
     * @param userGroupName
     * @return
     */
    Integer getTotalSize(String userGroupName);

    /**
     * 带条件的查询
     *
     * @param start
     * @param limit
     * @param namme
     * @param date
     * @param userGroupName
     * @return
     */
    List<TransformationEntity> conditionFindTrans(int start, int limit, String namme, String date, String userGroupName);

    /**
     * 带条件查询总记录数
     *
     * @param name
     * @param date
     * @param userGroupName
     * @return
     */
    Integer conditionFindTransCount(String name, String date, String userGroupName);

    /**
     * 根据名称查询Trans
     *
     * @param transName
     * @return
     */
    TransformationEntity getTransByName(String transName);

    /**
     * 更新
     * @param oldName
     * @param newName
     */
    void updateTransNameforTrans(String oldName, String newName);

}

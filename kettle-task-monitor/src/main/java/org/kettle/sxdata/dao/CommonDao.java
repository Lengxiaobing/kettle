package org.kettle.sxdata.dao;

import org.kettle.sxdata.entity.DatabaseConnEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: CommonDao
 * @author: ZX
 * @date: 2018/11/20 15:14
 */
@Repository("taskCommonDao")
public interface CommonDao {
    /**
     * 得到Dababases Conn
     *
     * @return
     */
    List<DatabaseConnEntity> getDababasesConn();

    /**
     * 删除数据库Attr
     *
     * @param id
     */
    void deleteDatabaseAttr(Integer id);

    /**
     * 删除数据库元
     *
     * @param id
     */
    void deleteDatabaseMeta(Integer id);

    /**
     * 删除作业数据库
     *
     * @param id
     */
    void deleteJobDatabase(Integer id);

    /**
     * 删除Trans数据库
     *
     * @param id
     */
    void deleteTransDatabase(Integer id);
}

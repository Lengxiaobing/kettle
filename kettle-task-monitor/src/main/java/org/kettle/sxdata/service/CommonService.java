package org.kettle.sxdata.service;

import org.kettle.sxdata.entity.DatabaseConnEntity;

import java.util.List;

/**
 * @description: CommonService
 * @author: ZX
 * @date: 2018/11/20 17:14
 */
public interface CommonService {
    /**
     * 获取数据库
     *
     * @return
     */
    List<DatabaseConnEntity> getDatabases();

    /**
     * 删除数据库连接
     *
     * @param id
     */
    void deleteDatabaseConn(Integer id);
}


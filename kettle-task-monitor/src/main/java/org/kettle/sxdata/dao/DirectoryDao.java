package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.DirectoryEntity;

/**
 * @description: DirectoryDao
 * @author: ZX
 * @date: 2018/11/20 15:18
 */
@Repository
public interface DirectoryDao {
    /**
     * 按ID获取目录
     *
     * @param id
     * @return
     */
    DirectoryEntity getDirectoryById(Integer id);
}

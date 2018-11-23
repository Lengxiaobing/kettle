package org.kettle.sxdata.service.impl;

import org.kettle.sxdata.dao.CommonDao;
import org.kettle.sxdata.entity.DatabaseConnEntity;
import org.kettle.sxdata.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: CommonServiceImpl
 * @author: ZX
 * @date: 2018/11/20 16:24
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    @Qualifier("taskCommonDao")
    protected CommonDao cDao;

    @Override
    public List<DatabaseConnEntity> getDatabases() {
        return cDao.getDababasesConn();
    }

    @Override
    public void deleteDatabaseConn(Integer id) {
        cDao.deleteDatabaseAttr(id);
        cDao.deleteDatabaseMeta(id);
        cDao.deleteJobDatabase(id);
        cDao.deleteTransDatabase(id);
    }
}

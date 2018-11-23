package org.kettle.webapp.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;

/**
 * @description: 获取Sql进度
 * @author: ZX
 * @date: 2018/11/21 15:48
 */
public class GetSqlProgress {

    private static Class<?> PKG = GetSqlProgress.class;

    private TransMeta transMeta;

    public GetSqlProgress(TransMeta transMeta) {
        this.transMeta = transMeta;
    }

    public List<SQLStatement> run() throws InvocationTargetException, InterruptedException {
        try {
            return transMeta.getSQLStatements(null);
        } catch (KettleException e) {
            throw new InvocationTargetException(e, BaseMessages.getString(PKG, "GetSQLProgressDialog.RuntimeError.UnableToGenerateSQL.Exception", e.getMessage()));
        }
    }

}

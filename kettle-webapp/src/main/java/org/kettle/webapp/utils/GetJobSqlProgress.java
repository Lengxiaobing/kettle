package org.kettle.webapp.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.kettle.ext.App;
import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;

/**
 * @description: 获取Job SQL进度
 * @author: ZX
 * @date: 2018/11/21 15:46
 */
public class GetJobSqlProgress {

    private static Class<?> PKG = GetSqlProgress.class;

    private JobMeta jobMeta;

    public GetJobSqlProgress(JobMeta jobMeta) {
        this.jobMeta = jobMeta;
    }

    public List<SQLStatement> run() throws InvocationTargetException, InterruptedException {
        try {
            return jobMeta.getSQLStatements(App.getInstance().getRepository(), null);
        } catch (KettleException e) {
            throw new InvocationTargetException(e, BaseMessages.getString(PKG, "GetSQLProgressDialog.RuntimeError.UnableToGenerateSQL.Exception", e.getMessage()));
        }
    }
}

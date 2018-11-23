package org.kettle.ext.job.step;

import java.util.List;

import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.metastore.api.IMetaStore;

import com.mxgraph.model.mxCell;

/**
 * @description: 作业输入解码器
 * @author: ZX
 * @date: 2018/11/20 18:27
 */
public interface JobEntryDecoder {

    /**
     * 解码步骤
     *
     * @param cell
     * @param databases
     * @param metaStore
     * @return
     * @throws Exception
     */
    JobEntryCopy decodeStep(mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception;

}

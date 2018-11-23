package org.kettle.ext.job.step;

import org.pentaho.di.job.entry.JobEntryCopy;
import org.w3c.dom.Element;

/**
 * @description: 作业输入编码器
 * @author: ZX
 * @date: 2018/11/20 18:28
 */
public interface JobEntryEncoder {
    /**
     * 编码步骤
     *
     * @param jobEntry
     * @return
     * @throws Exception
     */
    Element encodeStep(JobEntryCopy jobEntry) throws Exception;

}

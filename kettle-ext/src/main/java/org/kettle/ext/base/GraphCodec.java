package org.kettle.ext.base;

import org.pentaho.di.base.AbstractMeta;

/**
 * @description: 图形编解码器
 * @author: ZX
 * @date: 2018/11/20 18:06
 */
public interface GraphCodec {

    /**
     * 转换编码
     */
    String TRANS_CODEC = "TransGraph";

    /**
     * 作业编码
     */
    String JOB_CODEC = "JobGraph";

    /**
     * 加密
     *
     * @param meta
     * @return
     * @throws Exception
     */
    String encode(AbstractMeta meta) throws Exception;

    /**
     * 解密
     *
     * @param graphXml
     * @return
     * @throws Exception
     */
    AbstractMeta decode(String graphXml) throws Exception;
}

package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 节点
 * @author: ZX
 * @date: 2018/11/20 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SlaveEntity {
    /**
     * 节点id
     */
    private Integer slaveId;
    private String name;
    /**
     * 节点的ip地址
     */
    private String hostName;
    private String username;
    private String password;
    /**
     * 端口
     */
    private String port;
    private String webappName;
    private String proxyHostname;
    private String proxyPort;
    private String nonproxyHosts;
    private char master;
    /**
     * 负载指数
     */
    private double loadAvg;
    /**
     * 状态
     */
    private String status;
    /**
     * 运行中的作业数
     */
    private Integer runningJobNum;
    /**
     * 运行中的转换数
     */
    private Integer runningTransNum;
    /**
     * 当天完成的作业数
     */
    private Integer finishJobNum;
    /**
     * 当天完成的转换数
     */
    private Integer finishTransNum;
    /**
     * 在线时长
     */
    private String upTime;

}

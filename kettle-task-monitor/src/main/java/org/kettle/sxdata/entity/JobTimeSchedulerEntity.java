package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 作业定时调度类 包含了定时调度的时间信息以及被调度的作业、节点等信息
 * @author: ZX
 * @date: 2018/11/20 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JobTimeSchedulerEntity {
    private long idJobtask;
    private Integer idJob;
    private String jobName;
    private String slaves;
    private String isrepeat;
    private Integer schedulertype;
    private Integer intervalminutes;
    private Integer hour;
    private Integer minutes;
    private Integer weekday;
    private Integer dayofmonth;
    /**
     * 定时信息的详细描述
     */
    private String timerInfo;
    /**
     * 节点ip
     */
    private String hostName;
    /**
     * 执行定时作业时的配置信息 不参与持久化
     */
    private String executionConfig;
    private String username;

}

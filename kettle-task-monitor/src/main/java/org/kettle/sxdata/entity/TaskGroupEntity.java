package org.kettle.sxdata.entity;

import lombok.*;

import java.util.Date;

/**
 * @description: 任务组实体
 *
 * @author:   ZX
 * @date:     2018/11/20 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskGroupEntity {
    private Integer taskGroupId;
    private String taskGroupName;
    private String taskGroupDesc;
    private Date createDate;
    /**
     * 标识是否包含某个任务  不参与数据的持久化
     */
    private String isContainsTask;

}

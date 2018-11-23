package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 任务组属性实体
 * @author: ZX
 * @date: 2018/11/20 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskGroupAttributeEntity {
    private Integer groupAttributeId;
    private String taskGroupName;
    private String type;
    private Integer taskId;
    private String taskName;
    private String taskPath;

}

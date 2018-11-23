package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 任务用户关系实体
 * @author: ZX
 * @date: 2018/11/20 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskUserRelationEntity {
    private Integer relationId;
    private String userGroupName;
    private String taskGroupName;
    private Integer type;

}

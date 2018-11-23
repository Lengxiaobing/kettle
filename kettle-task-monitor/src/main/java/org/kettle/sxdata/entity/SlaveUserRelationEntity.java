package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 从属用户关系实体
 * @author: ZX
 * @date: 2018/11/20 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SlaveUserRelationEntity {
    private Integer relationId;
    private String userGroupName;
    private Integer slaveId;
    private Integer type;
}

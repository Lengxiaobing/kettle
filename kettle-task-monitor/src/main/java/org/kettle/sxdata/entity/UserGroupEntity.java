package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 用户组实体
 *
 * @author:   ZX
 * @date:     2018/11/20 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserGroupEntity {
    private Integer userGroupId;
    private String userGroupName;
    private String userGroupDesc;

}

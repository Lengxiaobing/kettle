package org.kettle.sxdata.entity;

import lombok.*;

import java.util.Date;

/**
 * @description: 用户组属性实体
 * @author: ZX
 * @date: 2018/11/20 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserGroupAttributeEntity {
    private Integer groupAttributeId;
    private String userGroupName;
    private String userName;
    /**
     * 1:可增删改可执行 2:只读
     */
    private Integer slavePremissonType;
    /**
     * 1:可增删改 2:只读
     */
    private Integer taskPremissionType;
    /**
     * 1:管理员 2:普通用户
     */
    private Integer userType;
    private Date createDate;

}



package org.kettle.sxdata.entity;

import lombok.*;
import java.util.Date;

/**
 * @description: 用户实体
 * @author: ZX
 * @date: 2018/11/20 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity {
    private Integer userId;
    private String login;
    private String password;
    private String name;
    private String description;
    private char enabled;
    private Integer slavePower;
    private Integer taskGroupPower;
    private String belongToUserGroup;
    private Integer userType;
    private Date createDate;

}

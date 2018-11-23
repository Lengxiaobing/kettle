package org.kettle.sxdata.entity;

import lombok.*;

import java.util.Date;

/**
 * @description: 转换实体
 * @author: ZX
 * @date: 2018/11/20 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransformationEntity {
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改用户
     */
    private String modifiedUser;
    /**
     * 修改时间
     */
    private Date modifiedDate;
    private String name;
    /**
     * 创建用户
     */
    private String createUser;
    private Integer transformationId;
    /**
     * 与层级目录表主键关联 代表该转换存放于哪个目录下
     */
    private Integer directoryId;
    /**
     * 所在的直接父级目录名
     */
    private String directoryName;
    private String belongToTaskGroup;

}

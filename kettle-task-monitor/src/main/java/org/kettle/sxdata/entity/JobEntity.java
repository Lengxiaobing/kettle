package org.kettle.sxdata.entity;

import lombok.*;

import java.util.Date;

/**
 * @description: 作业实体
 * @author: ZX
 * @date: 2018/11/20 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class JobEntity {
    /**
     * 作业id
     */
    private Integer jobId;
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
    /**
     * 作业名称
     */
    private String name;
    /**
     * 创建用户
     */
    private String createUser;
    /**
     * 作业所在的目录的id,与层级目录表的id对应,用于标识该作业是存放在哪个目录,根目录是/
     */
    private Integer directoryId;
    /**
     * 作业的完整目录名
     */
    private String directoryName;
    /**
     * 所属任务组 多个以逗号形式分隔
     */
    private String belongToTaskGroup;

}

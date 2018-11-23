package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 任务控制实体
 * @author: ZX
 * @date: 2018/11/20 14:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TaskControlEntity {
    private String id;
    private String name;
    private String hostName;
    private String type;
    private String isStart;
    private String carteObjectId;

}

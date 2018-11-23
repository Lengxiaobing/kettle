package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 数据库连接实体
 *
 * @author:   ZX
 * @date:     2018/11/20 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DatabaseConnEntity {
    private String name;
    private String hostName;
    private String port;
    private String databaseName;
    private String databaseType;
    private Integer databaseId;
}

package org.kettle.sxdata.entity;

import lombok.*;

/**
 * @description: 资源库存放的层级目录实体
 *
 * @author:   ZX
 * @date:     2018/11/20 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DirectoryEntity {
    /**
     * 目录Id
     */
    private Integer directoryId;
    /**
     * 该目录的直接父级目录的id
     */
    private Integer parentDirectoryId;
    /**
     * 该目录的名称
     */
    private String directoryName;
}

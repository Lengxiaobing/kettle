package org.kettle.sxdata.entity;

import lombok.*;

import java.util.Date;

/**
 * @description: cart信息实体
 * @author: ZX
 * @date: 2018/11/20 14:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarteInfoEntity {
    private long carteRecordId;
    private Date nDate;
    private Integer slaveId;
    private Integer threadNum;
    private Integer jobNum;
    private Integer transNum;
    private Integer freeMem;
    private Integer totalMem;
    private String usedMemPercent;
    private Float loadAvg;
    private Integer finishedJobNum;
    private Integer finishedTransNum;
    private String hostFreeMem;
    private String hostCpuUsage;
    private String hostFreeDisk;
    private String xHour;
}

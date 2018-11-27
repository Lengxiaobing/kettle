package org.kettle.sxdata.util.quartz;

import org.apache.ibatis.session.SqlSession;
import org.kettle.sxdata.entity.CarteInfoEntity;
import org.kettle.sxdata.entity.SlaveEntity;
import org.kettle.sxdata.util.common.StringDateUtil;
import org.kettle.sxdata.util.task.CarteClient;
import org.kettle.sxdata.util.task.CarteStatusVo;
import org.kettle.sxdata.util.task.KettleEncr;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 从属配额
 * @author: ZX
 * @date: 2018/11/20 14:11
 */
public class SlaveQuota {

    /**
     * 每隔N分钟采集节点指标的具体业务方法(已在配置文件中整合Spring会自动调用)
     *
     * @throws Exception
     */
    public static void quotaSlaveInfoRepeat() throws Exception {
        SqlSession session = CarteClient.sessionFactory.openSession();
        List<CarteInfoEntity> carteInfoList = new ArrayList<>();
        // 采集所有节点的信息
        List<SlaveEntity> slaves = session.selectList("org.kettle.sxdata.dao.SlaveDao.getAllSlave", "");
        String nDate = StringDateUtil.dateToString(new java.util.Date(), "yyyy-MM-dd HH:mm:ss");

        String carteStatus;

        for (SlaveEntity slave : slaves) {
            slave.setPassword(KettleEncr.decryptPasswd(slave.getPassword()));
            CarteClient cc = new CarteClient(slave);

            carteStatus = cc.getStatusOrNull();
            if (carteStatus == null) {
                continue;
            }

            CarteStatusVo vo = CarteStatusVo.parseXml(carteStatus);
            //组装节点指标对象
//            String hostInfo = cc.getSlaveHostInfo();
            // 空闲内存
//            String memFree = hostInfo.split("\\$")[0];
            // 磁盘
//            String diskFree = hostInfo.split("\\$")[1];
            // cpu使用率
//            String cpuUsage = hostInfo.split("\\$")[2];
            // 第三方工具
//            String extraLib = hostInfo.split("\\$")[3];

            CarteInfoEntity carteInfo = CarteInfoEntity.builder()
                    .nDate(StringDateUtil.stringToDate(nDate, "yyyy-MM-dd HH:mm:ss"))
                    .slaveId(cc.getSlave().getSlaveId())
                    .threadNum(vo.getThreadCount())
                    .jobNum(vo.getRunningJobNum())
                    .transNum(vo.getRunningTransNum())
                    .freeMem((int) vo.getFreeMem())
                    .totalMem((int) vo.getTotalMem())
                    .usedMemPercent(vo.getFreeMemPercent())
                    .loadAvg((float) vo.getLoadAvg())
                    .finishedJobNum(null)
                    .finishedTransNum(null)
//                    .hostFreeMem(memFree)
//                    .hostCpuUsage(cpuUsage)
//                    .hostFreeDisk(diskFree)
                    .build();
            carteInfoList.add(carteInfo);
        }

        //把采集到的所有节点的当前指标信息更新到数据表中
        Integer result = 0;
        for (CarteInfoEntity carteInfoEntity : carteInfoList) {
            result += session.insert("org.kettle.sxdata.dao.CarteInfoDao.insertCarteInfo", carteInfoEntity);
        }
        session.commit();
        session.close();
    }
}

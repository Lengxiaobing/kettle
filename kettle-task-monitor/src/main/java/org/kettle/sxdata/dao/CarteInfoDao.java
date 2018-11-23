package org.kettle.sxdata.dao;

import org.springframework.stereotype.Repository;
import org.kettle.sxdata.entity.CarteInfoEntity;

import java.util.List;

/**
 * @description: CarteInfoDao
 * @author: ZX
 * @date: 2018/11/20 15:14
 */
@Repository
public interface CarteInfoDao {
    /**
     * 添加Carte信息
     *
     * @param carteInfo
     */
    void insertCarteInfo(CarteInfoEntity carteInfo);

    /**
     * 获取所有slave的quato
     *
     * @param minDate
     * @param maxDate
     * @return
     */
    List<CarteInfoEntity> getAllSlaveQuato(String minDate, String maxDate);

    /**
     * 通过Slave Id获得Slave Quato
     *
     * @param minDate
     * @param maxDate
     * @param slaveId
     * @return
     */
    List<CarteInfoEntity> getSlaveQuatoBySlaveId(String minDate, String maxDate, Integer slaveId);

    /**
     * 通过Avg获得Slave Quato
     *
     * @param minDate
     * @param maxDate
     * @param slaveId
     * @return
     */
    List<CarteInfoEntity> slaveQuatoByAvg(String minDate, String maxDate, Integer slaveId);

    /**
     * 通过Max获得Slave Quato
     *
     * @param minDate
     * @param maxDate
     * @param slaveId
     * @return
     */
    List<CarteInfoEntity> slaveQuatoByMax(String minDate, String maxDate, Integer slaveId);
}

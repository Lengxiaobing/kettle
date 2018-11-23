package org.kettle.sxdata.service.impl;

import net.sf.json.JSONObject;
import org.kettle.sxdata.dao.HadoopClusterDao;
import org.kettle.sxdata.entity.SlaveEntity;
import org.kettle.sxdata.service.HadoopService;
import org.kettle.sxdata.util.common.StringDateUtil;
import org.pentaho.big.data.impl.cluster.NamedClusterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: HadoopServiceImpl
 * @author: ZX
 * @date: 2018/11/20 16:28
 */
@Service
public class HadoopServiceImpl implements HadoopService {

    @Autowired
    @Qualifier("taskHadoopClusterDao")
    protected HadoopClusterDao hdpDao;

    @Override
    public synchronized String addHadoopCluster(NamedClusterImpl cluster) throws Exception {
        String success = "SUCCESS";
        Date date = new Date();
        cluster.setLastModifiedDate(date.getTime());
        //判断集配置名是否存在
        List<SlaveEntity> slaves = hdpDao.getClusters();
        if (null != slaves) {
            for (SlaveEntity slave : slaves) {
                if (slave.getName().equals(cluster.getName())) {
                    success = "faile";
                    return success;
                }
            }
        }
        //添加
        Map<String, Object> elementMap = new HashMap<>();
        Integer eleId = hdpDao.getNextId("r_element", "ID_ELEMENT");
        if (null == eleId) {
            eleId = 0;
        } else {
            eleId += 1;
        }
        elementMap.put("id", eleId);
        elementMap.put("type", 301);
        elementMap.put("name", cluster.getName());
        hdpDao.addClusterEle(elementMap);

        Class cls = cluster.getClass();
        Integer attrId = hdpDao.getNextId("r_element_attribute", "ID_ELEMENT_ATTRIBUTE");
        if (null == attrId) {
            attrId = 0;
        } else {
            attrId += 1;
        }
        for (Field field : cls.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.getName().equals("variables")) {
                continue;
            }
            String fieldName = field.getName();

            String getMethodName = "";
            if (fieldName.equals("mapr")) {
                getMethodName = "is" + StringDateUtil.firstLetterUpp(fieldName);
            } else {
                getMethodName = "get" + StringDateUtil.firstLetterUpp(fieldName);
            }
            try {
                String attrKey = "";
                String attrValue = "";
                Method m1 = cls.getMethod(getMethodName);
                Object fieldValue = m1.invoke(cluster);
                attrKey = fieldName;
                if (fieldValue instanceof Boolean) {
                    if ((Boolean) fieldValue) {
                        attrValue = "Y";
                    } else {
                        attrValue = "N";
                    }
                } else if (fieldValue instanceof Long) {
                    attrValue = String.valueOf(fieldValue);
                } else {
                    attrValue = fieldValue.toString();
                }
                hdpDao.addCluster(attrId, eleId, 0, attrKey, attrValue);
                attrId++;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        return success;
    }

    @Override
    public List<NamedClusterImpl> getAllCluster() {
        List<NamedClusterImpl> result = hdpDao.allCluster();
        return result;
    }

    @Override
    public NamedClusterImpl getCluster(String name) {
        return hdpDao.getClusterByName(name);
    }

    @Override
    public void deleteCluster(String clusterName) {
        Integer elementId = hdpDao.getEleIdByClusterName(clusterName);
        hdpDao.deleteEle(elementId);
        hdpDao.deleteEleAttr(elementId);
    }

    @Override
    public Integer getEleIdByClusterName(String clusterName) {
        return hdpDao.getEleIdByClusterName(clusterName);
    }

    @Override
    public String updateHadoopCluster(HttpServletRequest request) throws Exception {
        String success = "SUCCESS";
        JSONObject obj = JSONObject.fromObject(request.getParameter("cluster"));
        NamedClusterImpl cluster = (NamedClusterImpl) JSONObject.toBean(obj, NamedClusterImpl.class);
        Date date = new Date();
        cluster.setLastModifiedDate(date.getTime());
        Integer elementId = Integer.valueOf(request.getParameter("elementId"));
        //判断集配置名是否存在
        List<SlaveEntity> slaves = hdpDao.getClusters();
        if (null != slaves) {
            for (SlaveEntity slave : slaves) {
                if (elementId.equals(slave.getSlaveId())) {
                    continue;
                }
                if (slave.getName().equals(cluster.getName())) {
                    success = "faile";
                    return success;
                }
            }
        }
        //修改
        Map<String, Object> eleMap = new HashMap<>();
        eleMap.put("clusterName", cluster.getName());
        eleMap.put("elementId", elementId);
        hdpDao.updateEle(eleMap);

        Class cls = cluster.getClass();
        for (Field field : cls.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.getName().equals("variables")) {
                continue;
            }
            String fieldName = field.getName();
            String getMethodName = "";
            if (fieldName.equals("mapr")) {
                getMethodName = "is" + StringDateUtil.firstLetterUpp(fieldName);
            } else {
                getMethodName = "get" + StringDateUtil.firstLetterUpp(fieldName);
            }
            try {
                String attrKey = "";
                String attrValue = "";
                Method m1 = cls.getMethod(getMethodName);
                Object fieldValue = m1.invoke(cluster);
                attrKey = fieldName;
                if (fieldValue instanceof Boolean) {
                    if ((Boolean) fieldValue) {
                        attrValue = "Y";
                    } else {
                        attrValue = "N";
                    }
                } else if (fieldValue instanceof Long) {
                    attrValue = String.valueOf(fieldValue);
                } else {
                    attrValue = fieldValue.toString();
                }

                Map<String, Object> attrMap = new HashMap<>();
                attrMap.put("elementId", elementId);
                attrMap.put("attrValue", attrValue);
                attrMap.put("attrKey", attrKey);
                hdpDao.updateEleAttr(attrMap);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        return success;
    }
}

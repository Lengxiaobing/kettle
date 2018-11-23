package org.kettle.sxdata.service.impl;

import net.sf.json.JSONObject;
import org.kettle.ext.task.ExecutionTraceEntity;
import org.kettle.sxdata.bean.PageforBean;
import org.kettle.sxdata.dao.ExecutionTraceDao;
import org.kettle.sxdata.dao.TaskGroupDao;
import org.kettle.sxdata.entity.TaskGroupAttributeEntity;
import org.kettle.sxdata.service.HistoryLogService;
import org.kettle.sxdata.util.common.StringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: HistoryLogServiceImpl
 *
 * @author:   ZX
 * @date:     2018/11/20 16:31
 */
@Service
public class HistoryLogServiceImpl implements HistoryLogService {
    @Autowired
    @Qualifier("taskExecutionTraceDao")
    private ExecutionTraceDao executionTraceDao;

    @Autowired
    @Qualifier("taskTaskGroupDao")
    private TaskGroupDao groupDao;

    @Override
    public String getAllHistoryLog(int start, int limit,String statu,String type,String startDate,String taskName,String userGroupName) throws Exception{
        List<ExecutionTraceEntity> traces=executionTraceDao.getAllLogByPage(start,limit,statu,type,startDate,taskName,userGroupName);
        for(ExecutionTraceEntity trace:traces){
            if(trace.getStatus().equals("成功")){
                trace.setStatus("<font color='green'>"+trace.getStatus()+"</font>");
            }else{
                trace.setStatus("<font color='red'>"+trace.getStatus()+"</font>");
            }
        }

        PageforBean json=new PageforBean();
        json.setTotalProperty(executionTraceDao.getAllLogCount(statu,type,startDate,taskName,userGroupName));
        json.setRoot(traces);

        return JSONObject.fromObject(json, StringDateUtil.configJson("yyyy-MM-dd HH:mm:ss")).toString();
    }

    @Override
    public String getExecutionTraceById(Integer id) throws Exception{
        ExecutionTraceEntity trace=executionTraceDao.getTraceById(id);
        //增加所属任务组属性
        String config=trace.getExecutionConfiguration();
        if(null!=config){
            JSONObject json=JSONObject.fromObject(config);
            List<TaskGroupAttributeEntity> groups=groupDao.getTaskGroupByTaskName(trace.getJobName(),trace.getType());
            if(null!=groups && groups.size()>0){
                String[] groupNames=new String[groups.size()];
                for(int i=0;i<groups.size();i++){
                    TaskGroupAttributeEntity group=groups.get(i);
                    groupNames[i]=group.getTaskGroupName();
                }
                json.put("group",groupNames);
            }else{
                json.put("group","暂未分配任务组");
            }
            trace.setExecutionConfiguration(json.toString());
            trace.setExecutionLog(trace.getExecutionLog().replaceAll("\\\\n","<br/>"));
        }
        return JSONObject.fromObject(trace).toString();
    }
}

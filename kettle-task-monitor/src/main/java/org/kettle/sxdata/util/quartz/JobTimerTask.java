package org.kettle.sxdata.util.quartz;

import org.kettle.ext.JobExecutor;
import org.kettle.sxdata.entity.UserEntity;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @description: 定时作业具体方法
 * @author: ZX
 * @date: 2018/11/20 14:09
 */
public class JobTimerTask implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobExecutor jobExecutor = (JobExecutor) context.getJobDetail().getJobDataMap().get("jobExecutor");
        UserEntity user = (UserEntity) context.getJobDetail().getJobDataMap().get("loginUser");
        try {
//            //使用与carte服务器交互的方式执行定时作业
//            CarteClient cc = new CarteClient(slave);
//            //拼接资源库名
//            String repoId = CarteClient.hostName + "_" + CarteClient.databaseName;
//            //节点执行作业的请求
//            String urlString = "/?rep=" + repoId + "&user=" + user.getLogin() + "&pass=" + user.getPassword() + "&job=" + path + "&level=Basic";
//            urlString = Const.replace(urlString, "/", "%2F");
//            urlString = cc.getHttpUrl() + CarteClient.EXECREMOTE_JOB + urlString;
//            CarteTaskManager.addTask(cc, "job_exec", urlString);

            //调用kettle api方式执行定时作业
            Thread tr = new Thread(jobExecutor, "JobExecutor_" + jobExecutor.getExecutionId());
            tr.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

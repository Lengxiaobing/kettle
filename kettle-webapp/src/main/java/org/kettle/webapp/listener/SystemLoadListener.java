package org.kettle.webapp.listener;

import org.kettle.ext.core.PropsUI;
import org.kettle.sxdata.util.task.CarteTaskManager;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * @description: 系统负载监听器
 * @author: ZX
 * @date: 2018/11/21 15:10
 */
public class SystemLoadListener implements ServletContextListener {

    /**
     * 上下文被摧毁
     *
     * @param context
     */
    @Override
    public void contextDestroyed(ServletContextEvent context) {

    }

    /**
     * 上下文初始化
     *
     * @param context
     */
    @Override
    public void contextInitialized(ServletContextEvent context) {
        try {
            System.out.println("开启carte服务线程...");
            //启动1个线程处理执行carte服务
            CarteTaskManager.startThread(1);
            // 日志缓冲不超过5000行，缓冲时间不超过720秒
            KettleLogStore.init(5000, 720);
            KettleEnvironment.init();
            PropsUI.init("KettleWebConsole", Props.TYPE_PROPERTIES_KITCHEN);
            File path = new File("samples/repository");
            KettleFileRepositoryMeta meta = new KettleFileRepositoryMeta();
            meta.setBaseDirectory(path.getAbsolutePath());
            meta.setDescription("default");
            meta.setName("default");
            meta.setReadOnly(false);
            meta.setHidingHiddenFiles(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

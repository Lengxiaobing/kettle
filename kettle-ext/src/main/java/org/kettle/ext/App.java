package org.kettle.ext;

import org.apache.commons.dbcp.BasicDataSource;
import org.kettle.ext.core.PropsUI;
import org.pentaho.di.core.DBCache;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.job.JobExecutionConfiguration;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.pentaho.di.trans.TransExecutionConfiguration;
import org.pentaho.metastore.stores.delegate.DelegatingMetaStore;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * @description: 初始加载设置
 * @author: ZX
 * @date: 2018/11/21 11:40
 */
public class App implements ApplicationContextAware {

    private static App app;
    public static KettleDatabaseRepositoryMeta meta;

    private LogChannelInterface log;
    private TransExecutionConfiguration transExecutionConfiguration;
    private TransExecutionConfiguration transPreviewExecutionConfiguration;
    private TransExecutionConfiguration transDebugExecutionConfiguration;
    private JobExecutionConfiguration jobExecutionConfiguration;
    public PropsUI props;

    private App() {
        props = PropsUI.getInstance();
        log = new LogChannel(PropsUI.getAppName());
        loadSettings();

        transExecutionConfiguration = new TransExecutionConfiguration();
        transExecutionConfiguration.setGatheringMetrics(true);
        transPreviewExecutionConfiguration = new TransExecutionConfiguration();
        transPreviewExecutionConfiguration.setGatheringMetrics(true);
        transDebugExecutionConfiguration = new TransExecutionConfiguration();
        transDebugExecutionConfiguration.setGatheringMetrics(true);

        jobExecutionConfiguration = new JobExecutionConfiguration();

        variables = new RowMetaAndData(new RowMeta());
    }

    public void loadSettings() {
        LogLevel logLevel = LogLevel.getLogLevelForCode(props.getLogLevel());
        DefaultLogLevel.setLogLevel(logLevel);
        log.setLogLevel(logLevel);
        KettleLogStore.getAppender().setMaxNrLines(props.getMaxNrLinesInLog());
        DBCache.getInstance().setActive(props.useDBCache());
    }

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    private Repository repository;

    private Repository defaultRepository;

    private DelegatingMetaStore metaStore;

    private RowMetaAndData variables;

    private ArrayList<String> arguments = new ArrayList<>();

    public Repository getRepository() {
        return repository;
    }

    public Repository getDefaultRepository() {
        return this.defaultRepository;
    }

    public void selectRepository(Repository repo) {
        if (repository != null) {
            repository.disconnect();
        }
        repository = repo;
    }

    public DelegatingMetaStore getMetaStore() {
        return metaStore;
    }

    public LogChannelInterface getLog() {
        return log;
    }

    public String[] getArguments() {
        return arguments.toArray(new String[arguments.size()]);
    }

    public JobExecutionConfiguration getJobExecutionConfiguration() {
        return jobExecutionConfiguration;
    }

    public TransExecutionConfiguration getTransDebugExecutionConfiguration() {
        return transDebugExecutionConfiguration;
    }

    public TransExecutionConfiguration getTransPreviewExecutionConfiguration() {
        return transPreviewExecutionConfiguration;
    }

    public TransExecutionConfiguration getTransExecutionConfiguration() {
        return transExecutionConfiguration;
    }

    public RowMetaAndData getVariables() {
        return variables;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        KettleDatabaseRepository repository = new KettleDatabaseRepository();
        try {
            BasicDataSource dataSource = (BasicDataSource) context.getBean(DataSource.class);
            DatabaseMeta dbMeta = new DatabaseMeta();

            String url = dataSource.getUrl();
            String hostname = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
            String port = url.substring(url.lastIndexOf(":") + 1, url.lastIndexOf("/"));
            String dbName = url.substring(url.lastIndexOf("/") + 1);
            String databaseType = dataSource.getConnection().getMetaData().getDatabaseProductName();

            dbMeta.setName(hostname);
            dbMeta.setDBName(dbName);
            dbMeta.setDatabaseType(databaseType);
            dbMeta.setAccessType(0);
            dbMeta.setHostname(hostname);
            dbMeta.setServername(hostname);
            dbMeta.setDBPort(port);
            dbMeta.setUsername(dataSource.getUsername());
            dbMeta.setPassword(dataSource.getPassword());
            dbMeta.setObjectId(new LongObjectId(100));
            dbMeta.setShared(true);
            dbMeta.addExtraOption(databaseType, "characterEncoding", "utf8");
            dbMeta.addExtraOption(databaseType, "characterSetResults", "utf8");
            dbMeta.addExtraOption(databaseType, "useUnicode", "true");
            dbMeta.addExtraOption(databaseType, "autoReconnect", "true");
            dbMeta.addExtraOption(databaseType, "useSSL", "true");

            meta = new KettleDatabaseRepositoryMeta();
            meta.setName(hostname);
            meta.setId("KettleDatabaseRepository");
            meta.setConnection(dbMeta);
            meta.setDescription(hostname);

            repository.init(meta);
            repository.connect("admin", "admin");
            this.repository = repository;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     *
     * @return
     */
    public Repository reConnect() {
        Repository appRepo = App.getInstance().getRepository();
        try {
            appRepo.disconnect();
            appRepo.init(App.meta);
            appRepo.connect("admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appRepo;
    }
}

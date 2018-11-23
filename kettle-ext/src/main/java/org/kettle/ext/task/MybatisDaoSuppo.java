package org.kettle.ext.task;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: Mybatis支持
 * @author: ZX
 * @date: 2018/11/21 11:22
 */
public class MybatisDaoSuppo implements ApplicationContextAware {
    public static DefaultSqlSessionFactory sessionFactory;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BasicDataSource dataSource = (BasicDataSource) context.getBean("dataSource");
        sessionFactory = (DefaultSqlSessionFactory) context.getBean("sqlSessionFactory");
    }

}

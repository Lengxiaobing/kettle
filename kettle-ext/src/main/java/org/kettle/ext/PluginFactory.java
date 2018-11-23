package org.kettle.ext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @description: 插件工厂
 * @author: ZX
 * @date: 2018/11/21 11:42
 */
public class PluginFactory implements ApplicationContextAware {
    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    public static Object getBean(String beanId) {
        return context.getBean(beanId);
    }

    public static boolean containBean(String beanId) {
        return context.containsBean(beanId);
    }

}

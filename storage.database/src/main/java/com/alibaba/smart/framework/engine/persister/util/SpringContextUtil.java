package com.alibaba.smart.framework.engine.persister.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author 高海军 帝奇 Apr 19, 2015 12:40:57 PM
 */
public class SpringContextUtil implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext appContext;
    private static ConfigurableListableBeanFactory factory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        SpringContextUtil.factory = factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext c) throws BeansException {
        SpringContextUtil.appContext = c;
    }

    public static Object getBean(String name) {
        return appContext.getBean(name);
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }

    public static void setAppContext(ApplicationContext appContext) {
        appContext = appContext;
    }

    public static ConfigurableListableBeanFactory getFactory() {
        return factory;
    }

    public static void setFactory(ConfigurableListableBeanFactory factory) {
        factory = factory;
    }

}

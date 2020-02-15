package com.alibaba.smart.framework.engine.persister.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author 高海军 帝奇 Apr 19, 2015 12:40:57 PM
 */
@Service
public class SpringContextUtil implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext applicationContext;
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        SpringContextUtil.beanFactory = factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext c) throws BeansException {
        SpringContextUtil.applicationContext = c;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T  getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name,requiredType);
    }

    public static ApplicationContext getAppContext() {
        return applicationContext;
    }

    public static void setAppContext(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    public static ConfigurableListableBeanFactory getFactory() {
        return beanFactory;
    }

    public static void setFactory(ConfigurableListableBeanFactory factory) {
        beanFactory = factory;
    }

}

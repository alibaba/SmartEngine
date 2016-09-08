package com.alibaba.smart.framework.engine.configuration.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration,InitializingBean,ApplicationContextAware {

    private ExtensionPointRegistry   extensionPointRegistry;

    private static ApplicationContext applicationContext;




    public static Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }



    @Override
    public void afterPropertiesSet() throws Exception {

        System.out.printf("spring init");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        DefaultProcessEngineConfiguration.applicationContext = applicationContext;
    }
}

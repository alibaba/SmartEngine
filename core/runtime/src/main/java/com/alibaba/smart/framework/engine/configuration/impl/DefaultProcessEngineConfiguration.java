package com.alibaba.smart.framework.engine.configuration.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 * @author 高海军 帝奇  2016.11.11
 * TODO 支持多种模式 不持久,内存,数据库等。
 */
@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration,InitializingBean,ApplicationContextAware {

    private static final Logger LOGGER          = LoggerFactory.getLogger(DefaultProcessEngineConfiguration.class);


    private ExtensionPointRegistry   extensionPointRegistry;


    //TODO 核心引擎不依赖 Spring
    private static ApplicationContext applicationContext;




    public  Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }



    @Override
    public void afterPropertiesSet() throws Exception {

        LOGGER.debug("spring init");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        DefaultProcessEngineConfiguration.applicationContext = applicationContext;
    }
}

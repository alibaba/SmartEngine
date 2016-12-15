package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration,InitializingBean,ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(DefaultProcessEngineConfiguration.class);

    private ExtensionPointRegistry   extensionPointRegistry;

    private static ApplicationContext applicationContext;




    public  Object getBean(String bean) {
        return applicationContext.getBean(bean);
    }



    @Override
    public void afterPropertiesSet() throws Exception {

        logger.info("smartengine sping model inti finish");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        DefaultProcessEngineConfiguration.applicationContext = applicationContext;
    }
}

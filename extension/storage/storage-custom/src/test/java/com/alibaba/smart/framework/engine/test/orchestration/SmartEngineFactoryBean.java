package com.alibaba.smart.framework.engine.test.orchestration;

import java.io.InputStream;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultInstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.util.ClassUtil;
import com.alibaba.smart.framework.engine.util.IOUtil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class SmartEngineFactoryBean implements FactoryBean<SmartEngine>, InitializingBean, ApplicationContextAware {

    private SmartEngine smartEngine;

    //private InstanceAccessor defaultInstanceAccessService = new DefaultInstanceAccessor();

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SmartEngineFactoryBean.applicationContext = applicationContext;
    }

    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setInstanceAccessor(new CustomInstanceAccessService());

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        deployProcessDefinition();
    }

    private void deployProcessDefinition() {
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath*:/smart-engine/*.xml");
            for (Resource resource : resources) {
                InputStream inputStream = resource.getInputStream();
                repositoryCommandService.deploy(inputStream);
                IOUtil.closeQuietly(inputStream);
            }
        } catch (Exception e) {
            throw new EngineException(e);
        }

    }

    @Override
    public SmartEngine getObject() throws Exception {
        return smartEngine;
    }

    @Override
    public Class<?> getObjectType() {
        return SmartEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private class CustomInstanceAccessService implements InstanceAccessor {

        @Override
        public Object access(String classNameOrBeanName) {
            try {
                Class clazz = ClassUtil.getContextClassLoader().loadClass(classNameOrBeanName);
                Object bean = applicationContext.getBean(clazz);
                return bean;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
package com.alibaba.smart.framework.engine.test.orchestration;

import java.io.InputStream;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultInstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.util.IOUtil;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class SmartEngineFactoryBean implements FactoryBean<SmartEngine>, InitializingBean {

    private SmartEngine smartEngine;

    private InstanceAccessor defaultInstanceAccessService = new DefaultInstanceAccessor();

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
                Class clazz = ClassLoaderUtil.getContextClassLoader().loadClass(classNameOrBeanName);
                Object bean = ApplicationContextUtil.getBean(clazz);
                return bean;
            } catch (NoSuchBeanDefinitionException e) {
                Object bean = defaultInstanceAccessService.access(classNameOrBeanName);
                return bean;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
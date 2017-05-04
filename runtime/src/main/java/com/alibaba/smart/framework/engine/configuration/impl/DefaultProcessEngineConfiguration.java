package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.common.service.InstanceAccessService;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.instance.util.DefaultInstanceAccessService;

import lombok.Data;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;

/**
 * @author 高海军 帝奇  2016.11.11
 */
@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration //, InitializingBean, ApplicationContextAware
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessEngineConfiguration.class);

    private ExtensionPointRegistry extensionPointRegistry;

    private ExceptionProcessor exceptionProcessor;

    private TaskAssigneeService taskAssigneeService;

    private InstanceAccessService instanceAccessService;

    public DefaultProcessEngineConfiguration() {
        //说明:先默认设置一个id生成器,业务使用方可以根据自己的需要再覆盖掉这个值。
        this.idGenerator = new DefaultIdGenerator();
        this.instanceAccessService = new DefaultInstanceAccessService();
    }

    @Setter
    private IdGenerator idGenerator;

    @Override
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }




//    private PersisterStrategy persisterStrategy;

}

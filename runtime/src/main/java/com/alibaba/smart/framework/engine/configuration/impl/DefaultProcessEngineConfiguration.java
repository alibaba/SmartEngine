package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;

import lombok.Data;
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
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessEngineConfiguration.class);

    private ExtensionPointRegistry extensionPointRegistry;

    private IdGenerator idGenerator;

    private InstanceAccessor instanceAccessor;

    private ExceptionProcessor exceptionProcessor;

    private TaskAssigneeDispatcher taskAssigneeDispatcher;

    private VariablePersister variablePersister;

    private MultiInstanceCounter multiInstanceCounter;

    private LockStrategy lockStrategy;
    //protected boolean persisteVariableInstanceEnabled = false;

    private TableSchemaStrategy tableSchemaStrategy;

    public DefaultProcessEngineConfiguration() {
        //说明:先默认设置一个id生成器,业务使用方可以根据自己的需要再覆盖掉这个值。
        this.idGenerator = new DefaultIdGenerator();
        this.instanceAccessor = new DefaultInstanceAccessor();
        this.variablePersister = new DefaultVariablePersister();
        this.tableSchemaStrategy = new DefaultTableSchemaStrategy();

    }




}

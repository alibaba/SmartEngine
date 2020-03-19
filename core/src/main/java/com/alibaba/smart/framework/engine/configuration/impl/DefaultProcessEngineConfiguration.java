package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.concurrent.ExecutorService;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.DelegationExecutor;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.configuration.OptionContainer;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.impl.option.DefaultOptionContainer;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 */
@Data
public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration
{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProcessEngineConfiguration.class);

    private SmartEngine smartEngine;

    private IdGenerator idGenerator;

    private InstanceAccessor instanceAccessor;

    private DelegationExecutor delegationExecutor;

    private AnnotationScanner annotationScanner;

    private ExceptionProcessor exceptionProcessor;

    private TaskAssigneeDispatcher taskAssigneeDispatcher;

    private VariablePersister variablePersister;

    private MultiInstanceCounter multiInstanceCounter;

    private LockStrategy lockStrategy;

    private TableSchemaStrategy tableSchemaStrategy;

    private ExecutorService executorService;

    private boolean expressionCompileResultCached;

    private OptionContainer optionContainer ;

    public DefaultProcessEngineConfiguration() {
        //说明:先默认设置一个id生成器,业务使用方可以根据自己的需要再覆盖掉这个值。
        this.idGenerator = new DefaultIdGenerator();
        this.instanceAccessor = new DefaultInstanceAccessor();
        this.delegationExecutor = new DefaultDelegationExecutor();
        this.annotationScanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());
        this.variablePersister = new DefaultVariablePersister();
        this.tableSchemaStrategy = new DefaultTableSchemaStrategy();
        this.optionContainer = new DefaultOptionContainer();
        optionContainer.put(ConfigurationOption.EXPRESSION_COMPILE_RESULT_CACHED_OPTION);
    }




}

package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.common.expression.evaluator.MvelExpressionEvaluator;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.*;
import com.alibaba.smart.framework.engine.configuration.impl.option.DefaultOptionContainer;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.SmartBase;
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

    private ExpressionEvaluator expressionEvaluator;

    private DelegationExecutor delegationExecutor;

    private ListenerExecutor listenerExecutor;

    private AnnotationScanner annotationScanner;

    private ExceptionProcessor exceptionProcessor;

    private ParallelServiceOrchestration parallelServiceOrchestration;

    private TaskAssigneeDispatcher taskAssigneeDispatcher;

    private VariablePersister variablePersister;

    private MultiInstanceCounter multiInstanceCounter;

    private LockStrategy lockStrategy;

    private TableSchemaStrategy tableSchemaStrategy;

    private ExecutorService executorService;

    private Map<String, ExecutorService> executorServiceMap;

    private PvmActivityTaskFactory pvmActivityTaskFactory;

    private boolean expressionCompileResultCached;

    private OptionContainer optionContainer ;

    private Map<String,Object> magicExtension;

    public DefaultProcessEngineConfiguration() {
        //说明:先默认设置一个id生成器,业务使用方可以根据自己的需要再覆盖掉这个值。
        this.idGenerator = new DefaultIdGenerator();
        this.exceptionProcessor = new DefaultExceptionProcessor();
        this.instanceAccessor = new DefaultInstanceAccessor();
        this.delegationExecutor = new DefaultDelegationExecutor();
        this.expressionEvaluator = new MvelExpressionEvaluator();
        this.parallelServiceOrchestration = new DefaultParallelServiceOrchestration();
        this.listenerExecutor = new DefaultListenerExecutor();
        this.annotationScanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());
        this.variablePersister = new DefaultVariablePersister();

        this.pvmActivityTaskFactory = new DefaultPvmActivityTaskFactory();

        this.tableSchemaStrategy = new DefaultTableSchemaStrategy();
        this.optionContainer = new DefaultOptionContainer();
        optionContainer.put(ConfigurationOption.EXPRESSION_COMPILE_RESULT_CACHED_OPTION);

        buildDefaultSupportNameSpace();
    }

    private void buildDefaultSupportNameSpace() {
        Map<String,Object> magicExtension = MapUtil.newHashMap();

        Map<String,String> tuples = new HashMap<String, String>();
        tuples.put(SmartBase.SMART_NS,"");

        //兼容主流开源的 namespace，便于兼容他们配套的前端设计器
        tuples.put(BpmnNameSpaceConstant.CAMUNDA_NAME_SPACE,"smart");
        tuples.put(BpmnNameSpaceConstant.FLOWABLE_NAME_SPACE,"flowable");
        tuples.put(BpmnNameSpaceConstant.ACTIVITI_NAME_SPACE,"activiti");

        magicExtension.put("fallBack",tuples);

        this.setMagicExtension(magicExtension);
    }

}

package com.alibaba.smart.framework.engine.configuration;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.annotation.Experiment;
import com.alibaba.smart.framework.engine.common.expression.evaluator.ExpressionEvaluator;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;

/**
 * @author 高海军 帝奇  2016.11.11
 *
 *
 */
public interface ProcessEngineConfiguration {

    SmartEngine getSmartEngine();

    void setSmartEngine(SmartEngine smartEngine);
    /**
     * 主要用于外部扩展。
     * 在生产环境下必须设置。
     * 强烈建议该接口返回的是不重复的 id。
     * @param idGenerator
     *
     */
    void setIdGenerator(IdGenerator idGenerator);

    IdGenerator getIdGenerator();

    /**
     * 主要用于外部扩展。
     * 在生产环境下必须设置。
     * 在系统集成中，SmartEngine 为了和外部系统方便的交互，则必须要提供相应的机制来获取外部服务对象。
     * 该机制主要用于 smart:class 机制。
     * @param instanceAccessor
     *
     */
    void setInstanceAccessor(InstanceAccessor instanceAccessor);

    InstanceAccessor getInstanceAccessor();

    /**
     * 默认不需要扩展。默认使用MVEL表达式引擎。
     *
     * @param expressionEvaluator
     *
     */
    void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator);

    ExpressionEvaluator getExpressionEvaluator();

    /**
     * 默认不需要扩展。
     */
    void setDelegationExecutor(DelegationExecutor delegationExecutor);

    DelegationExecutor getDelegationExecutor();
    /**
     * 默认不需要扩展。
     */
    void setListenerExecutor(ListenerExecutor listenerExecutor);

    ListenerExecutor getListenerExecutor();

    /**
     * 默认不需要扩展。但是生产环境也可以使用Spring的AnnotationScanner实现。
     */
    void setAnnotationScanner(AnnotationScanner annotationScanner);


    AnnotationScanner getAnnotationScanner();

    /**
     * 主要用于外部扩展。
     * 可选扩展。
     * 设计目的是用来处理各种 Delegation 内部抛出去的异常。
     * @param exceptionProcessor
     *
     */
    void setExceptionProcessor(ExceptionProcessor exceptionProcessor);

    ExceptionProcessor getExceptionProcessor();


    /**
     * 一般不需要扩展。
     * 但当你需要针对Delegation执行时抛出的异常，超时等做出精细化处理时，才需要扩展此类。
     */
    void setParallelServiceOrchestration(ParallelServiceOrchestration parallelServiceOrchestration);

    ParallelServiceOrchestration getParallelServiceOrchestration();

    /**
     * 主要用于外部扩展。
     * 可选扩展。
     * 设计目的是用来处理任务的处理者。
     * 该扩展主要用于 database模式下的 userTask 场景。
     * @param taskAssigneeDispatcher
     */
    void setTaskAssigneeDispatcher(TaskAssigneeDispatcher taskAssigneeDispatcher);

    TaskAssigneeDispatcher getTaskAssigneeDispatcher();

    /**
     * 主要用于持久化变量数据。
     * @param variablePersister
     */
    void setVariablePersister(VariablePersister variablePersister);

    VariablePersister getVariablePersister();

    /**
     * 用于会签场景的扩展。
     * @param multiInstanceCounter
     */
    void setMultiInstanceCounter(MultiInstanceCounter multiInstanceCounter);

    MultiInstanceCounter getMultiInstanceCounter();

    void setLockStrategy(LockStrategy lockStrategy);

    LockStrategy getLockStrategy();

    /**
     * 目前仅用于 MongoDB 模式
     *
     * @param tableSchemaStrategy
     */
    @Experiment
    void setTableSchemaStrategy(TableSchemaStrategy tableSchemaStrategy);

    TableSchemaStrategy getTableSchemaStrategy();

    /**
     * 仅用于并行网关的并发fork行为。
     * @param executorService
     */
    void setExecutorService(ExecutorService executorService);

    ExecutorService getExecutorService();

    /**
     * 设置自定义的线程池map，支持并行网关的fork时，指定自定义的线程池
     * @param poolsMap
     */
    void setExecutorServiceMap(Map<String, ExecutorService> poolsMap);

    Map<String, ExecutorService> getExecutorServiceMap();

    void setOptionContainer(OptionContainer optionContainer);

    OptionContainer getOptionContainer();

    /**
     * 目前使用场景较少，主要用于兼容老版本的xml namespace 场景。
     * 一般使用场景下，直接忽略该配置即可
     * @param extension
     */
    void setMagicExtension(Map<String,Object> extension);

    Map<String,Object> getMagicExtension();

    // 是否要干掉 用于配置扩展,默认可以为空。设计目的是根据自己的业务需求,来自定义存储(该机制会绕过引擎自带的各种Storage机制,powerful and a little UnSafe)。。
    //void setPersisterStrategy(PersisterStrategy persisterStrategy);
    //
    //PersisterStrategy getPersisterStrategy();
}


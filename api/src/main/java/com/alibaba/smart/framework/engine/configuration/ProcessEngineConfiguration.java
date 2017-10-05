package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.common.service.InstanceAccessService;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

/**
 * @author 高海军 帝奇  2016.11.11
 *
 *
 */
public interface ProcessEngineConfiguration {

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
     * @param instanceAccessService
     *
     */
    void setInstanceAccessService(InstanceAccessService instanceAccessService);

    InstanceAccessService getInstanceAccessService();

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
     * 主要用于外部扩展。
     * 可选扩展。
     * 设计目的是用来处理任务的处理者。
     * 该扩展主要用于 database模式下的 userTask 场景。
     * @param taskAssigneeService
     */
    void setTaskAssigneeService(TaskAssigneeService taskAssigneeService);

    TaskAssigneeService getTaskAssigneeService();

    /**
     * 内部执行需要，不需要客户端 API 感知。
     * @param extensionPointRegistry
     */
    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);

    ExtensionPointRegistry getExtensionPointRegistry();


    // 是否要干掉 用于配置扩展,默认可以为空。设计目的是根据自己的业务需求,来自定义存储(该机制会绕过引擎自带的各种Storage机制,powerful and a little UnSafe)。。
    //void setPersisterStrategy(PersisterStrategy persisterStrategy);
    //
    //PersisterStrategy getPersisterStrategy();
}


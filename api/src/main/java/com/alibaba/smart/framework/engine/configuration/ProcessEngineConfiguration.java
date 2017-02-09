package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface ProcessEngineConfiguration {


    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);


//    //TODO 删除
//    Object getBean(String bean);



    //用于配置扩展,默认可以为空。设计目的是用来处理ReceiverTask,ServiceTask内部跑出去的异常。
    void setExceptionProcessor(ExceptionProcessor exceptionProcessor);

    ExceptionProcessor getExceptionProcessor();

    //用于配置扩展,默认可以为空。设计目的是用来处理任务的处理者。
    void setTaskAssigneeService(TaskAssigneeService taskAssigneeService);

    TaskAssigneeService getTaskAssigneeService();


    //用于配置扩展,默认可以为空。设计目的是根据自己的业务需求,来自定义存储(该机制会绕过引擎自带的各种Storage机制,powerful and a little UnSafe)。。
//    void setPersisterStrategy(PersisterStrategy persisterStrategy);
//
//    PersisterStrategy getPersisterStrategy();
}


package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface ProcessEngineConfiguration {

    void setExceptionProcessor(ExceptionProcessor exceptionProcessor);

    ExceptionProcessor getExceptionProcessor();


    void setTaskAssigneeService(TaskAssigneeService taskAssigneeService);

    TaskAssigneeService getTaskAssigneeService();

    ExtensionPointRegistry getExtensionPointRegistry();

    void setExtensionPointRegistry(ExtensionPointRegistry extensionPointRegistry);

    void setPersisteModel(String persisteModel);




    String getPersisteModel();

    Object getBean(String bean);
}

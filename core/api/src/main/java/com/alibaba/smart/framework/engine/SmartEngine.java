package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.ExecutionService;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {

    
    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryService getRepositoryService();

    ProcessService getProcessService();

    ExecutionService getExecutionService();

    TaskService getTaskService();

    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destory();

    //TODO 干掉 
    ClassLoader getClassLoader(String moduleName);
}

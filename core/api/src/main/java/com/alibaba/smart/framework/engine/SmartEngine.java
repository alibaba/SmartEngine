package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {

    
    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryCommandService getRepositoryService();

    ProcessCommandService getProcessService();

    ExecutionCommandService getExecutionService();

    TaskCommandService getTaskService();

    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();

    //TODO 干掉 
    ClassLoader getClassLoader(String moduleName);
}

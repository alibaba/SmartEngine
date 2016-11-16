package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {


    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryCommandService getRepositoryService();

    ProcessCommandService getProcessService();

    ExecutionCommandService getExecutionCommandService();

    TaskCommandService getTaskCommandService();

    TaskQueryService getTaskQueryService();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}

package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {


    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryCommandService getRepositoryService();

    ProcessCommandService getProcessService();

    ExecutionCommandService getExecutionCommandService();

    TaskCommandService getTaskCommandService();

    ProcessInstanceQueryService getProcessQueryService();

    ActivityInstanceQueryService getActivityQueryService();

    ExecutionInstanceQueryService getExecutionQueryService();

    TaskInstanceQueryService getTaskQueryService();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}
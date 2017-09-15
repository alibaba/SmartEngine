package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskInstanceCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {


    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);


    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryCommandService getRepositoryCommandService();

    ProcessCommandService getProcessCommandService();

    ExecutionCommandService getExecutionCommandService();

    TaskInstanceCommandService getTaskCommandService();

    ProcessInstanceQueryService getProcessQueryService();

    ActivityInstanceQueryService getActivityQueryService();

    ExecutionInstanceQueryService getExecutionQueryService();

    TaskInstanceQueryService getTaskQueryService();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}

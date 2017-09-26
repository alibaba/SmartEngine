package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * @author 高海军 帝奇
 */
public interface SmartEngine {


    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);




    ExtensionPointRegistry getExtensionPointRegistry();

    RepositoryCommandService getRepositoryCommandService();

    DeploymentCommandService getDeploymentCommandService();

    ProcessCommandService getProcessCommandService();

    ExecutionCommandService getExecutionCommandService();

    TaskCommandService getTaskCommandService();

    DeploymentQueryService getDeploymentQueryService();

    ProcessQueryService getProcessQueryService();

    ActivityQueryService getActivityQueryService();

    ExecutionQueryService getExecutionQueryService();

    TaskQueryService getTaskQueryService();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}

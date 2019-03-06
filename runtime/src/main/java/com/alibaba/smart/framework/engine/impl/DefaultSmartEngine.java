package com.alibaba.smart.framework.engine.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskItemCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultSmartEngine implements SmartEngine {


    private ExtensionPointRegistry extensionPointRegistry;

    @Getter
    @Setter
    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration) {

        this.setProcessEngineConfiguration(processEngineConfiguration);

        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);
        this.extensionPointRegistry.register();
        this.extensionPointRegistry.start();

        processEngineConfiguration.setExtensionPointRegistry(extensionPointRegistry);
    }

    @Override
    public void destroy() {
        this.extensionPointRegistry.stop();
    }


    @Override
    public RepositoryCommandService getRepositoryCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryCommandService.class);
    }

    @Override
    public RepositoryQueryService getRepositoryQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryQueryService.class);
    }

    @Override
    public DeploymentCommandService getDeploymentCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(DeploymentCommandService.class);
    }

    @Override
    public ProcessCommandService getProcessCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(ProcessCommandService.class);
    }

    @Override
    public ExecutionCommandService getExecutionCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
    }

    @Override
    public TaskCommandService getTaskCommandService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskCommandService.class);
    }

    @Override
    public DeploymentQueryService getDeploymentQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(DeploymentQueryService.class);
    }

    @Override
    public ProcessQueryService getProcessQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(ProcessQueryService.class);
    }

    @Override
    public ActivityQueryService getActivityQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(ActivityQueryService.class);
    }

    @Override
    public ExecutionQueryService getExecutionQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(ExecutionQueryService.class);
    }

    @Override
    public TaskQueryService getTaskQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskQueryService.class);
    }

    @Override
    public VariableQueryService getVariableQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(VariableQueryService.class);
    }

    @Override
    public TaskAssigneeQueryService getTaskAssigneeQueryService() {
        return  this.extensionPointRegistry.getExtensionPoint(TaskAssigneeQueryService.class);
    }

    @Override
    public TaskItemCommandService getTaskItemCommandService() {
        return  this.extensionPointRegistry.getExtensionPoint(TaskItemCommandService.class);
    }

}

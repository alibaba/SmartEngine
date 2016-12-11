package com.alibaba.smart.framework.engine.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.impl.DefaultExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultSmartEngine implements SmartEngine {


    private ExtensionPointRegistry extensionPointRegistry;


    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration) {
        this.extensionPointRegistry = new DefaultExtensionPointRegistry(this);

        this.install();
        this.extensionPointRegistry.start();
    }

    private void install() throws EngineException {

       this.extensionPointRegistry.register();

    }


    @Override
    public void destroy() {
        this.extensionPointRegistry.stop();

    }


    @Override
    public RepositoryCommandService getRepositoryService() {
        return this.extensionPointRegistry.getExtensionPoint(RepositoryCommandService.class);
    }

    @Override
    public ProcessCommandService getProcessService() {
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
    public TaskInstanceQueryService getTaskQueryService() {
        return this.extensionPointRegistry.getExtensionPoint(TaskInstanceQueryService.class);
    }

    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

}

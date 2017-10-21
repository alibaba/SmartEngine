package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.Collection;
import java.util.List;

import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  17:19.
 */
public class DefaultRepositoryQueryService  implements RepositoryQueryService, LifeCycleListener {
    @Override
    public ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version) {
        return processDefinitionContainer.getProcessDefinition(processDefinitionId,version);
    }

    @Override
    public ProcessDefinition getCachedProcessDefinition(String processDefinitionIdAndVersion) {
        return processDefinitionContainer.getProcessDefinition(processDefinitionIdAndVersion);

    }

    @Override
    public Collection<ProcessDefinition> getAllCachedProcessDefinition() {
        return processDefinitionContainer.getProcessDefinitionConcurrentHashMap().values();

    }

    private ExtensionPointRegistry extensionPointRegistry;


    public DefaultRepositoryQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }


    private ProcessDefinitionContainer processDefinitionContainer;


    @Override
    public void start() {
        this.processDefinitionContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
    }

    @Override
    public void stop() {

    }
}
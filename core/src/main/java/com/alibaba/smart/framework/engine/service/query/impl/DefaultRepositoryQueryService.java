package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.Collection;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  17:19.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = RepositoryQueryService.class)

public class DefaultRepositoryQueryService  implements RepositoryQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {


    private ProcessDefinitionContainer processDefinitionContainer;


    @Override
    public ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version) {
        return this.getCachedProcessDefinition(processDefinitionId, version, null);
    }

    @Override
    public ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version,String tenantId) {
        return processDefinitionContainer.getProcessDefinition(processDefinitionId,version,tenantId);
    }

    @Override
    public ProcessDefinition getCachedProcessDefinition(String processDefinitionIdAndVersion) {
        return processDefinitionContainer.getProcessDefinition(processDefinitionIdAndVersion);

    }

    @Override
    public Collection<ProcessDefinition> getAllCachedProcessDefinition() {
        return processDefinitionContainer.getProcessDefinitionConcurrentHashMap().values();

    }


    @Override
    public void start() {
        this.processDefinitionContainer = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.SERVICE,ProcessDefinitionContainer.class);


    }

    @Override
    public void stop() {

    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}
package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:46.
 */
public class DefaultVariableQueryService implements VariableQueryService , LifeCycleListener {

    private final ProcessEngineConfiguration processEngineConfiguration;
    private ExtensionPointRegistry extensionPointRegistry;

    public DefaultVariableQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public List<VariableInstance> findProcessInstanceVariableList(String processInstanceId) {
        return findList(  processInstanceId, AdHocConstant.DEFAULT_ZERO_VALUE );}

    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        VariableInstanceStorage variableInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(VariableInstanceStorage.class);

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        List<VariableInstance> variableInstanceList =   variableInstanceStorage.findList(processInstanceId,executionInstanceId,variablePersister,processEngineConfiguration );

        return  variableInstanceList;
    }


}


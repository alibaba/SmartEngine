package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:46.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = VariableQueryService.class)

public class DefaultVariableQueryService implements VariableQueryService , LifeCycleHook,
    ProcessEngineConfigurationAware {

    private  ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void start() {

        this.variableInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON, VariableInstanceStorage.class);

    }


    @Override
    public void stop() {

    }

    @Override
    public List<VariableInstance> findProcessInstanceVariableList(String processInstanceId) {
        return this.findProcessInstanceVariableList(processInstanceId, null);
    }
    @Override
    public List<VariableInstance> findProcessInstanceVariableList(String processInstanceId,String tenantId) {
        return findList(  processInstanceId, AdHocConstant.DEFAULT_ZERO_VALUE ,tenantId);}

    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId) {
        return findList(processInstanceId, executionInstanceId, null);
    }
    @Override
    public List<VariableInstance> findList(String processInstanceId, String executionInstanceId,String tenantId) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        List<VariableInstance> variableInstanceList =   variableInstanceStorage.findList(processInstanceId,executionInstanceId,variablePersister,tenantId,processEngineConfiguration );

        return  variableInstanceList;
    }

    private VariableInstanceStorage variableInstanceStorage;
    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}


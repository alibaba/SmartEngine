package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.constant.AdHocConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.service.command.VariableCommandService;

/**
 * 主要变量插入。
 *
 * @author 高海军 帝奇  2021.02.25
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = VariableCommandService.class)
public class DefaultVariableCommandService   implements VariableCommandService , LifeCycleHook, ProcessEngineConfigurationAware {

    private  ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void insert(VariableInstance... variableInstances) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        for (VariableInstance instance : variableInstances) {
            variableInstanceStorage.insert(variablePersister,instance,processEngineConfiguration);
        }

    }


    @Override
    public void start() {

        this.variableInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON, VariableInstanceStorage.class);

    }


    @Override
    public void stop() {

    }



    private VariableInstanceStorage variableInstanceStorage;
    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}

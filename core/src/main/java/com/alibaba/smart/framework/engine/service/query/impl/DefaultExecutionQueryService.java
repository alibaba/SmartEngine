package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */

@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ExecutionQueryService.class)

public class DefaultExecutionQueryService implements ExecutionQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private ExecutionInstanceStorage executionInstanceStorage;

    @Override
    public void start() {

        this. executionInstanceStorage= processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public List<ExecutionInstance> findActiveExecutionList(String processInstanceId) {


        return executionInstanceStorage.findActiveExecution(processInstanceId, processEngineConfiguration);
    }

    @Override
    public List<ExecutionInstance> findAll(String processInstanceId) {
        return executionInstanceStorage.findAll(processInstanceId, processEngineConfiguration);
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}

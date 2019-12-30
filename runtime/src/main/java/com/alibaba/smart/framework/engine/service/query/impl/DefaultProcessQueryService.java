package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */

@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessQueryService.class)

public class DefaultProcessQueryService implements ProcessQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private ProcessInstanceStorage processInstanceStorage;



    @Override
    public void start() {

        this.processInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceStorage.class);


    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance findById(String processInstanceId) {

        return processInstanceStorage.findOne(processInstanceId, processEngineConfiguration);
    }

    @Override
    public List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam) {

        return processInstanceStorage.queryProcessInstanceList(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam) {

        return processInstanceStorage.count(processInstanceQueryParam,processEngineConfiguration );
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}

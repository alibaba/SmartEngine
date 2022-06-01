package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:46.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = TaskAssigneeQueryService.class)

public class DefaultTaskAssigneeQueryService implements TaskAssigneeQueryService, LifeCycleHook,
    ProcessEngineConfigurationAware {

    private   ProcessEngineConfiguration processEngineConfiguration;
    private TaskAssigneeStorage taskAssigneeStorage;



    @Override
    public void start() {

        this.taskAssigneeStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON, TaskAssigneeStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId) {


        List<TaskAssigneeInstance>  taskAssigneeStorageList =  taskAssigneeStorage.findList(taskInstanceId, processEngineConfiguration);
        return taskAssigneeStorageList;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList) {

        return taskAssigneeStorage.findAssigneeOfInstanceList(taskInstanceIdList,processEngineConfiguration );
    }

    @Override
    public List<TaskAssigneeInstance> findAll(String processInstanceId) {
        return taskAssigneeStorage.findAll(processInstanceId, processEngineConfiguration);
    }


    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}


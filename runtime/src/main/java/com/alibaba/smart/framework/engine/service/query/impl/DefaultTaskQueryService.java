package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultTaskQueryService implements TaskQueryService, LifeCycleHook {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessEngineConfiguration processEngineConfiguration ;

    public DefaultTaskQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
    }

    @Override
    public void start() {

//        this.taskInstanceStorage = this.extensionPointRegistry.getExtensionPoint(TaskInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.findPendingTaskList(pendingTaskQueryParam, processEngineConfiguration);
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {


        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.countPendingTaskList(pendingTaskQueryParam, processEngineConfiguration);
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        return taskInstanceStorage.findTaskListByAssignee(param, processEngineConfiguration);
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        return taskInstanceStorage.countTaskListByAssignee(param,processEngineConfiguration );
    }

    @Override
    public List<TaskInstance> findAllPendingTaskList(String processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);

        return taskInstanceStorage.findTaskByProcessInstanceIdAndStatus(taskInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public TaskInstance findOne(String taskInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        TaskInstance taskInstance = taskInstanceStorage.find(taskInstanceId, processEngineConfiguration);
        return taskInstance;
    }

    @Override
    public List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam){
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.findTaskList(taskInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        return taskInstanceStorage.count(taskInstanceQueryParam, processEngineConfiguration);
    }
}

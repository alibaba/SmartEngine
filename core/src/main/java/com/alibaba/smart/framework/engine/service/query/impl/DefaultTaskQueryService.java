package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = TaskQueryService.class)

public class DefaultTaskQueryService implements TaskQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private ProcessEngineConfiguration processEngineConfiguration ;
    private TaskInstanceStorage taskInstanceStorage;

    @Override
    public void start() {
        this.taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,TaskInstanceStorage.class);

    }



    @Override
    public void stop() {

    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {

        return taskInstanceStorage.findPendingTaskList(pendingTaskQueryParam, processEngineConfiguration);
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {



        return taskInstanceStorage.countPendingTaskList(pendingTaskQueryParam, processEngineConfiguration);
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        return taskInstanceStorage.findTaskListByAssignee(param, processEngineConfiguration);
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        return taskInstanceStorage.countTaskListByAssignee(param,processEngineConfiguration );
    }

    @Override
    public List<TaskInstance> findAllPendingTaskList(String processInstanceId) {
        return this.findAllPendingTaskList(processInstanceId, null);
    }

    @Override
    public List<TaskInstance> findAllPendingTaskList(String processInstanceId,String tenantId) {

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);

        return taskInstanceStorage.findTaskByProcessInstanceIdAndStatus(taskInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public TaskInstance findOne(String taskInstanceId) {
        return this.findOne(taskInstanceId,null);
    }

    @Override
    public TaskInstance findOne(String taskInstanceId,String tenantId) {
        TaskInstance taskInstance = taskInstanceStorage.find(taskInstanceId,tenantId, processEngineConfiguration);
        return taskInstance;
    }

    @Override
    public List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam){

        return taskInstanceStorage.findTaskList(taskInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam) {

        return taskInstanceStorage.count(taskInstanceQueryParam, processEngineConfiguration);
    }


    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}

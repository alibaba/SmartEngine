package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.CompletedTaskQueryParam;
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
        taskInstanceQueryParam.setTenantId(tenantId);

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
    public List<TaskInstance> findCompletedTaskList(CompletedTaskQueryParam param) {
        // 将CompletedTaskQueryParam转换为TaskInstanceQueryParam
        TaskInstanceQueryParam taskInstanceQueryParam = convertToTaskInstanceQueryParam(param);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.COMPLETED);
        
        return taskInstanceStorage.findTaskList(taskInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long countCompletedTaskList(CompletedTaskQueryParam param) {
        // 将CompletedTaskQueryParam转换为TaskInstanceQueryParam
        TaskInstanceQueryParam taskInstanceQueryParam = convertToTaskInstanceQueryParam(param);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.COMPLETED);
        
        return taskInstanceStorage.count(taskInstanceQueryParam, processEngineConfiguration);
    }

    /**
     * 将CompletedTaskQueryParam转换为TaskInstanceQueryParam
     */
    private TaskInstanceQueryParam convertToTaskInstanceQueryParam(CompletedTaskQueryParam param) {
        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        
        taskInstanceQueryParam.setTenantId(param.getTenantId());
        taskInstanceQueryParam.setPageOffset(param.getPageOffset());
        taskInstanceQueryParam.setPageSize(param.getPageSize());
        
        taskInstanceQueryParam.setClaimUserId(param.getClaimUserId());
        taskInstanceQueryParam.setProcessInstanceIdList(param.getProcessInstanceIdList());
        taskInstanceQueryParam.setTitle(param.getTitle());
        taskInstanceQueryParam.setTag(param.getTag());
        taskInstanceQueryParam.setComment(param.getComment());
        
        // 处理流程定义类型
        if (param.getProcessDefinitionTypes() != null && !param.getProcessDefinitionTypes().isEmpty()) {
            // 这里简化处理，取第一个类型，实际可能需要扩展TaskInstanceQueryParam支持多个类型
            taskInstanceQueryParam.setProcessDefinitionType(param.getProcessDefinitionTypes().get(0));
        }

        // 映射完成时间范围
        taskInstanceQueryParam.setCompleteTimeStart(param.getCompleteTimeStart());
        taskInstanceQueryParam.setCompleteTimeEnd(param.getCompleteTimeEnd());

        return taskInstanceQueryParam;
    }


    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}

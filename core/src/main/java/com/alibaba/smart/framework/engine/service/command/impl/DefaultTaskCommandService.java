package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.TaskEventPublisher;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.AssigneeTypeConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskTransferRecord;
import com.alibaba.smart.framework.engine.instance.impl.DefaultAssigneeOperationRecord;
import com.alibaba.smart.framework.engine.instance.impl.DefaultRollbackRecord;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskTransferRecordStorage;
import com.alibaba.smart.framework.engine.instance.storage.AssigneeOperationRecordStorage;
import com.alibaba.smart.framework.engine.instance.storage.RollbackRecordStorage;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.*;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 高海军 帝奇  2016.11.11
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = TaskCommandService.class)

public class DefaultTaskCommandService implements TaskCommandService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTaskCommandService.class);

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;
    private ExecutionCommandService executionCommandService;
    private ProcessEngineConfiguration processEngineConfiguration;
    private TaskInstanceStorage taskInstanceStorage;
    private TaskAssigneeStorage taskAssigneeStorage;
    private TaskTransferRecordStorage taskTransferRecordStorage;
    private AssigneeOperationRecordStorage assigneeOperationRecordStorage;
    private RollbackRecordStorage rollbackRecordStorage;

    @Override
    public void start() {

        AnnotationScanner annotationScanner = this.processEngineConfiguration.getAnnotationScanner();
        this.executionCommandService = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,ExecutionCommandService.class);
        this.processInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceStorage.class);
        this.taskAssigneeStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskAssigneeStorage.class);
        this.activityInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceStorage.class);
        this.executionInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);
        this.taskInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,TaskInstanceStorage.class);

        // Initialize Storage for operation records
        this.taskTransferRecordStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, TaskTransferRecordStorage.class);
        this.assigneeOperationRecordStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, AssigneeOperationRecordStorage.class);
        this.rollbackRecordStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, RollbackRecordStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public void claim(String taskId, String userId, String tenantId) {
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, tenantId, processEngineConfiguration);
        if (taskInstance == null) {
            throw new ValidationException("Task instance not found for taskId: " + taskId);
        }
        if (taskInstance.getClaimUserId() != null && !taskInstance.getClaimUserId().isEmpty()) {
            throw new ValidationException("Task already claimed by user: " + taskInstance.getClaimUserId());
        }
        taskInstance.setClaimUserId(userId);
        taskInstance.setClaimTime(DateUtil.getCurrentDate());
        taskInstanceStorage.update(taskInstance, processEngineConfiguration);
        Map<String, Object> claimExtra = new HashMap<String, Object>();
        claimExtra.put("claimUserId", userId);
        fireTaskEvent(EventConstant.TASK_CLAIMED, taskInstance, tenantId, claimExtra);
    }

    @Override
    public ProcessInstance complete(String taskId, Map<String, Object> request, Map<String, Object> response) {

        String tenantId = null;
        if(null != request) {
            tenantId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }
        TaskInstance taskInstance = taskInstanceStorage.find(taskId,tenantId,processEngineConfiguration );
        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            request, taskInstanceStorage, processEngineConfiguration);

        fireTaskEvent(EventConstant.TASK_COMPLETED, taskInstance, tenantId, Collections.<String, Object>emptyMap());

        // 自动关闭该任务的所有督办
        try {
            SupervisionInstanceStorage supervisionStorage = (SupervisionInstanceStorage)
                processEngineConfiguration.getInstanceAccessor().access("supervisionInstanceStorage");
            if (supervisionStorage != null) {
                supervisionStorage.closeSupervisionByTask(taskInstance.getInstanceId(), tenantId, processEngineConfiguration);
            }
        } catch (Exception e) {
            // 记录日志但不影响任务完成
            logger.warn("Failed to close supervision for task: " + taskId, e);
        }

       return  executionCommandService.signal(taskInstance.getExecutionInstanceId(), request,response);

    }

    @Override
    public ProcessInstance complete(String taskId, Map<String, Object> request) {
      return   this.complete(taskId,request,null);
    }

    @Override
    public ProcessInstance complete(String taskId, String userId, Map<String, Object> request) {
        if(null == request){
            request = new HashMap<String, Object>();
        }
        request.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);

        //TUNE check privilege

      return   complete(  taskId, request);
    }

    @Override
    public void transfer(String taskId, String fromUserId, String toUserId) {
        transfer(taskId,fromUserId,toUserId,null);
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, null, processEngineConfiguration);
        Map<String, Object> transferExtra = new HashMap<String, Object>();
        transferExtra.put("fromUserId", fromUserId);
        transferExtra.put("toUserId", toUserId);
        fireTaskEvent(EventConstant.TASK_TRANSFERRED, taskInstance, null, transferExtra);
    }
    @Override
    public void transfer(String taskId, String fromUserId, String toUserId,String tenantId) {

        ConfigurationOption configurationOption = processEngineConfiguration
            .getOptionContainer().get(ConfigurationOption.TRANSFER_ENABLED_OPTION.getId());

        if (!configurationOption.isEnabled()){
            throw new ValidationException("should set TRANSFER_ENABLED_OPTION ");
        }


        List<TaskAssigneeInstance> taskAssigneeInstanceList = taskAssigneeStorage.findList(taskId,tenantId,processEngineConfiguration );

        if(CollectionUtil.isEmpty(taskAssigneeInstanceList)){
            throw new ValidationException("taskAssigneeInstanceList can't be empty for taskId:"+taskId);
        }

        boolean found = false;

        TaskAssigneeInstance matchedTaskAssigneeInstance = null;
        for (TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
            if (taskAssigneeInstance.getAssigneeId().equals(fromUserId)){
                found =true;
                matchedTaskAssigneeInstance = taskAssigneeInstance;
                break;
            }
        }

        if(!found){
            throw new ValidationException("No taskAssigneeInstance found  for fromUserId:"+fromUserId);
        }

        if( !AssigneeTypeConstant.USER.equals( matchedTaskAssigneeInstance.getAssigneeType())){
            throw new ValidationException("Illegal  AssigneeType :"+matchedTaskAssigneeInstance.getAssigneeType());
        }

        taskAssigneeStorage.update(matchedTaskAssigneeInstance.getInstanceId(),toUserId,tenantId,processEngineConfiguration);

    }

    @Override
    public TaskInstance createTask(ExecutionInstance executionInstance, String taskInstanceStatus,Map<String, Object> request) {
    	IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
    	
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());
        taskInstance.setProcessDefinitionActivityId(executionInstance.getProcessDefinitionActivityId());
        taskInstance.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());

        taskInstance.setStatus(taskInstanceStatus);
        idGenerator.generate(taskInstance);

        if(null != request){

            String processDefinitionType = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE));
            taskInstance.setProcessDefinitionType(processDefinitionType);

            Date startTime = ObjectUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.TASK_START_TIME));
            taskInstance.setStartTime(startTime);

            Date completeTime = ObjectUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.TASK_COMPLETE_TIME));
            taskInstance.setCompleteTime(completeTime);

            String claimUserId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.CLAIM_USER_ID));
            taskInstance.setClaimUserId(claimUserId);

            Date claimTime = ObjectUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.CLAIM_USER_TIME));
            taskInstance.setClaimTime(claimTime);

            String tag = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG));
            taskInstance.setTag(tag);

        }

        InstanceUtil.enrich(request, taskInstance);

        //reAssign
        taskInstance = taskInstanceStorage.insert(taskInstance,processEngineConfiguration);

        return taskInstance;
    }


    @Override
    public void addTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance) {

        TaskInstance taskInstance = taskInstanceStorage.find(taskId,tenantId,processEngineConfiguration );

        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();

        taskAssigneeInstance.setTenantId(tenantId);
        taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());
        taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());

        taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());

        processEngineConfiguration.getIdGenerator().generate(taskAssigneeInstance);

        Date currentDate = DateUtil.getCurrentDate();
        taskAssigneeInstance.setStartTime(currentDate);
        taskAssigneeInstance.setCompleteTime(currentDate);

        taskAssigneeStorage.insert(taskAssigneeInstance,processEngineConfiguration);


    }

    @Override
    public void removeTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance) {


        List<TaskAssigneeInstance> taskAssigneeInstanceList = taskAssigneeStorage.findList(taskId,tenantId,processEngineConfiguration );

        boolean found = false;
        TaskAssigneeInstance matchedTaskAssigneeInstance = null;
        if(CollectionUtil.isNotEmpty(taskAssigneeInstanceList)){

            for (TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
                if(taskAssigneeInstance.getAssigneeId().equals(taskAssigneeCandidateInstance.getAssigneeId())
                    && taskAssigneeInstance.getAssigneeType().equals(taskAssigneeCandidateInstance.getAssigneeType())){
                    found = true;
                    matchedTaskAssigneeInstance = taskAssigneeInstance;
                    break;
                }
            }

            if(!found){
                throw new ValidationException("No taskAssigneeCandidateInstance found for "+taskAssigneeCandidateInstance);
            }

            taskAssigneeStorage.remove(matchedTaskAssigneeInstance.getInstanceId(),tenantId,processEngineConfiguration);


        }


    }

    @Override
    public void markDone(String taskId, Map<String, Object> request) {

        String tenantId = null;
        if(null != request) {
            tenantId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }
        TaskInstance taskInstance = taskInstanceStorage.find(taskId,tenantId,processEngineConfiguration );
        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            request, taskInstanceStorage, processEngineConfiguration);

    }





    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @Override
    public void transferWithReason(String taskId, String fromUserId, String toUserId, String reason, String tenantId) {
        // 执行原有的转交逻辑
        transfer(taskId, fromUserId, toUserId, tenantId);

        // 记录转交操作
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        DefaultTaskTransferRecord record = new DefaultTaskTransferRecord();
        idGenerator.generate(record);
        record.setTaskInstanceId(taskId);
        record.setFromUserId(fromUserId);
        record.setToUserId(toUserId);
        record.setTransferReason(reason);
        record.setTenantId(tenantId);

        taskTransferRecordStorage.insert(record, processEngineConfiguration);
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, tenantId, processEngineConfiguration);
        Map<String, Object> transferExtra2 = new HashMap<String, Object>();
        transferExtra2.put("fromUserId", fromUserId);
        transferExtra2.put("toUserId", toUserId);
        transferExtra2.put("reason", reason != null ? reason : "");
        fireTaskEvent(EventConstant.TASK_TRANSFERRED, taskInstance, tenantId, transferExtra2);
    }

    @Override
    public ProcessInstance rollbackTask(String taskId, String targetActivityId, String reason, String tenantId) {
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, tenantId, processEngineConfiguration);

        if (taskInstance == null) {
            throw new ValidationException("Task instance not found for taskId: " + taskId);
        }

        // 获取流程实例
        ProcessInstance processInstance = processInstanceStorage.findOne(
            taskInstance.getProcessInstanceId(), tenantId, processEngineConfiguration);

        String currentActivityId = taskInstance.getProcessDefinitionActivityId();

        // 记录回退操作（在执行回退之前）
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        DefaultRollbackRecord record = new DefaultRollbackRecord();
        idGenerator.generate(record);
        record.setProcessInstanceId(processInstance.getInstanceId());
        record.setTaskInstanceId(taskInstance.getInstanceId());
        record.setRollbackType("specific");
        record.setFromActivityId(currentActivityId);
        record.setToActivityId(targetActivityId);
        record.setOperatorUserId(taskInstance.getClaimUserId()); // 使用当前任务处理人作为操作人
        record.setRollbackReason(reason);
        record.setTenantId(tenantId);

        rollbackRecordStorage.insert(record, processEngineConfiguration);

        // 执行回退
        return executionCommandService.jumpTo(
            taskInstance.getProcessInstanceId(),
            processInstance.getProcessDefinitionId(),
            processInstance.getProcessDefinitionVersion(),
            processInstance.getStatus(),
            targetActivityId,
            tenantId
        );
    }

    @Override
    public void addTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason) {
        // 执行原有的加签逻辑
        addTaskAssigneeCandidate(taskId, tenantId, taskAssigneeCandidateInstance);

        // 记录加签操作
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        DefaultAssigneeOperationRecord record = new DefaultAssigneeOperationRecord();
        idGenerator.generate(record);
        record.setTaskInstanceId(taskId);

        // 获取任务实例以获取操作人
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, tenantId, processEngineConfiguration);
        if (taskInstance != null) {
            record.setOperatorUserId(taskInstance.getClaimUserId());
        }

        record.setOperationType("add_assignee");
        record.setTargetUserId(taskAssigneeCandidateInstance.getAssigneeId());
        record.setOperationReason(reason);
        record.setTenantId(tenantId);

        assigneeOperationRecordStorage.insert(record, processEngineConfiguration);
        if (taskInstance != null) {
            Map<String, Object> delegateExtra = new HashMap<String, Object>();
            delegateExtra.put("newAssigneeId", taskAssigneeCandidateInstance.getAssigneeId());
            delegateExtra.put("reason", reason != null ? reason : "");
            fireTaskEvent(EventConstant.TASK_DELEGATED, taskInstance, tenantId, delegateExtra);
        }
    }

    @Override
    public void removeTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason) {
        // 执行原有的减签逻辑
        removeTaskAssigneeCandidate(taskId, tenantId, taskAssigneeCandidateInstance);

        // 记录减签操作
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        DefaultAssigneeOperationRecord record = new DefaultAssigneeOperationRecord();
        idGenerator.generate(record);
        record.setTaskInstanceId(taskId);

        // 获取任务实例以获取操作人
        TaskInstance taskInstance = taskInstanceStorage.find(taskId, tenantId, processEngineConfiguration);
        if (taskInstance != null) {
            record.setOperatorUserId(taskInstance.getClaimUserId());
        }

        record.setOperationType("remove_assignee");
        record.setTargetUserId(taskAssigneeCandidateInstance.getAssigneeId());
        record.setOperationReason(reason);
        record.setTenantId(tenantId);

        assigneeOperationRecordStorage.insert(record, processEngineConfiguration);
        if (taskInstance != null) {
            Map<String, Object> revokeExtra = new HashMap<String, Object>();
            revokeExtra.put("removedAssigneeId", taskAssigneeCandidateInstance.getAssigneeId());
            revokeExtra.put("reason", reason != null ? reason : "");
            fireTaskEvent(EventConstant.TASK_REVOKED, taskInstance, tenantId, revokeExtra);
        }
    }

    private void fireTaskEvent(EventConstant event, TaskInstance taskInstance,
                                String tenantId, Map<String, Object> extra) {
        TaskEventPublisher publisher = processEngineConfiguration.getTaskEventPublisher();
        if (publisher != null) {
            publisher.publish(event, taskInstance, tenantId, extra != null ? extra : Collections.<String, Object>emptyMap());
        }
    }
}

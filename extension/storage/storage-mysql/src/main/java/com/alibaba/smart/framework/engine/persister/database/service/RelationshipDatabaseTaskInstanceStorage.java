package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceStorage.class)

public class RelationshipDatabaseTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return findTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam), processEngineConfiguration);
    }

    private TaskInstanceQueryByAssigneeParam convertToTaskInstanceQueryByAssigneeParam(PendingTaskQueryParam pendingTaskQueryParam) {
        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        taskInstanceQueryByAssigneeParam.setAssigneeGroupIdList(pendingTaskQueryParam.getAssigneeGroupIdList());
        taskInstanceQueryByAssigneeParam.setAssigneeUserId(pendingTaskQueryParam.getAssigneeUserId());
        taskInstanceQueryByAssigneeParam.setProcessDefinitionType(pendingTaskQueryParam.getProcessDefinitionType());

        List<String> processInstanceIdList = pendingTaskQueryParam.getProcessInstanceIdList();
        if(null != processInstanceIdList){
            List<Long> processInstanceIdList1  = new ArrayList<Long>(processInstanceIdList.size());
            for (String s : processInstanceIdList) {
                processInstanceIdList1.add(Long.valueOf(s));
            }
            taskInstanceQueryByAssigneeParam.setProcessInstanceIdList(processInstanceIdList1);
        }

        taskInstanceQueryByAssigneeParam.setPageOffset(pendingTaskQueryParam.getPageOffset());
        taskInstanceQueryByAssigneeParam.setPageSize(pendingTaskQueryParam.getPageSize());

        taskInstanceQueryByAssigneeParam.setStatus(TaskInstanceConstant.PENDING);
        return taskInstanceQueryByAssigneeParam;
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return countTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam),processEngineConfiguration );
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByAssignee(param);
        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }
        return taskInstanceList;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        Integer count = taskInstanceDAO.countTaskByAssignee(param);
        return  count  == null? 0L:count;
    }

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        String processInstanceId = taskInstanceQueryParam.getProcessInstanceIdList().get(0);
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByProcessInstanceIdAndStatus(
            Long.valueOf(processInstanceId),taskInstanceQueryParam.getStatus());

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }



    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskList(taskInstanceQueryParam);

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

          TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

          taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        Integer count = taskInstanceDAO.count(taskInstanceQueryParam);
        return  count  == null? 0L:count;
    }

    //@Override
    //public List<TaskInstance> findList(Long processInstanceId, Long activityInstanceId) {
    //    TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
    //    List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findList(processInstanceId ,activityInstanceId);
    //
    //    List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
    //    for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
    //            TaskInstance taskInstance= buildTaskInstance(taskInstanceEntity);
    //            taskInstanceList.add(taskInstance);
    //
    //    }
    //
    //    return taskInstanceList;
    //}

    @Override
    public TaskInstance insert(TaskInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");

        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.insert(taskInstanceEntity);

        Long entityId = taskInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf( taskInstance.getInstanceId());
        }

        taskInstanceEntity = taskInstanceDAO.findOne(entityId);

        //reAssign
        TaskInstance   resultTaskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        resultTaskInstance.setTaskAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());

        return resultTaskInstance;
    }


    @Override
    public TaskInstance update(TaskInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.update(taskInstanceEntity);


        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        return taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);
    }

    @Override
    public TaskInstance find(String instanceId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(Long.valueOf(instanceId));
        if (taskInstanceEntity == null){
            return null;
        }
        return buildTaskInstanceFromEntity(taskInstanceEntity);
    }

    private TaskInstance buildTaskInstanceFromEntity(TaskInstanceEntity taskInstanceEntity) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(taskInstanceEntity.getId().toString());
        taskInstance.setStartTime(taskInstanceEntity.getGmtCreate());
        taskInstance.setProcessDefinitionIdAndVersion(taskInstanceEntity.getProcessDefinitionIdAndVersion());
        taskInstance.setProcessInstanceId(taskInstanceEntity.getProcessInstanceId().toString());
        taskInstance.setActivityInstanceId(taskInstanceEntity.getActivityInstanceId().toString());
        taskInstance.setProcessDefinitionType(taskInstanceEntity.getProcessDefinitionType());
        taskInstance.setTag(taskInstanceEntity.getTag());
        taskInstance.setStatus(taskInstanceEntity.getStatus());

        taskInstance.setProcessDefinitionActivityId(taskInstanceEntity.getProcessDefinitionActivityId());
        taskInstance.setExecutionInstanceId(taskInstanceEntity.getExecutionInstanceId().toString());

        taskInstance.setClaimUserId(taskInstanceEntity.getClaimUserId());
        taskInstance.setCompleteTime(taskInstanceEntity.getCompleteTime());
        taskInstance.setClaimTime(taskInstanceEntity.getClaimTime());
        taskInstance.setComment(taskInstanceEntity.getComment());
        taskInstance.setExtension(taskInstanceEntity.getExtension());
        taskInstance.setTitle(taskInstanceEntity.getTitle());
        taskInstance.setPriority(taskInstanceEntity.getPriority());
        return taskInstance;
    }

    private TaskInstanceEntity buildTaskInstanceEntity(TaskInstance taskInstance) {
        TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();

        taskInstanceEntity.setId(Long.valueOf(taskInstance.getInstanceId()));
        taskInstanceEntity.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
        taskInstanceEntity.setProcessInstanceId(Long.valueOf(taskInstance.getProcessInstanceId()));
        taskInstanceEntity.setActivityInstanceId(Long.valueOf(taskInstance.getActivityInstanceId()));
        taskInstanceEntity.setProcessDefinitionActivityId(taskInstance.getProcessDefinitionActivityId());
        taskInstanceEntity.setExecutionInstanceId(Long.valueOf(taskInstance.getExecutionInstanceId()));
        taskInstanceEntity.setClaimUserId(taskInstance.getClaimUserId());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        taskInstanceEntity.setStatus(taskInstance.getStatus());
        taskInstanceEntity.setCompleteTime(taskInstance.getCompleteTime());
        taskInstanceEntity.setPriority(taskInstance.getPriority());
        taskInstanceEntity.setTag(taskInstance.getTag());
        taskInstanceEntity.setProcessDefinitionType(taskInstance.getProcessDefinitionType());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        taskInstanceEntity.setComment(taskInstance.getComment());
        taskInstanceEntity.setTitle(taskInstance.getTitle());
        taskInstanceEntity.setExtension(taskInstance.getExtension());
        //taskInstanceEntity.setGmtModified(taskInstance.getCompleteTime());
        return taskInstanceEntity;
    }


    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        taskInstanceDAO.delete(Long.valueOf(instanceId));

    }
}

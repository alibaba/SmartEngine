package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.TaskInstanceBuilder;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.UserTaskIndexDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.UserTaskIndexEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceStorage.class)

public class RelationshipDatabaseTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return findTaskListByAssignee(TaskInstanceBuilder.convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam), processEngineConfiguration);
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return countTaskListByAssignee(TaskInstanceBuilder.convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam),processEngineConfiguration );
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        if (isShardingEnabled(processEngineConfiguration)) {
            return findTaskListByAssigneeFromIndex(param, processEngineConfiguration);
        }
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByAssignee(param);
        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= TaskInstanceBuilder.buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }
        return taskInstanceList;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        if (isShardingEnabled(processEngineConfiguration)) {
            UserTaskIndexDAO userTaskIndexDAO = (UserTaskIndexDAO) processEngineConfiguration
                .getInstanceAccessor().access("userTaskIndexDAO");
            Integer count = userTaskIndexDAO.countByAssignee(param);
            return count == null ? 0L : count;
        }
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        Integer count = taskInstanceDAO.countTaskByAssignee(param);
        return  count  == null? 0L:count;
    }

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        Object processInstanceIdObj = taskInstanceQueryParam.getProcessInstanceIdList().get(0);
        Long processInstanceId = processInstanceIdObj instanceof Long
            ? (Long) processInstanceIdObj
            : Long.valueOf(processInstanceIdObj.toString());
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByProcessInstanceIdAndStatus(
            processInstanceId, taskInstanceQueryParam.getStatus(),taskInstanceQueryParam.getTenantId());

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= TaskInstanceBuilder.buildTaskInstanceFromEntity(taskInstanceEntity);

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

          TaskInstance taskInstance= TaskInstanceBuilder.buildTaskInstanceFromEntity(taskInstanceEntity);

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

        TaskInstanceEntity taskInstanceEntity = TaskInstanceBuilder.buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.insert(taskInstanceEntity);

        Long entityId = taskInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf( taskInstance.getInstanceId());
        }

        taskInstanceEntity = taskInstanceDAO.findOne(entityId,taskInstance.getTenantId());

        //reAssign
        TaskInstance   resultTaskInstance= TaskInstanceBuilder.buildTaskInstanceFromEntity(taskInstanceEntity);
        resultTaskInstance.setTaskAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());

        return resultTaskInstance;
    }


    @Override
    public TaskInstance update(TaskInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = TaskInstanceBuilder.buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.update(taskInstanceEntity);

        if (isShardingEnabled(processEngineConfiguration)) {
            String status = taskInstance.getStatus();
            if (TaskInstanceConstant.COMPLETED.equals(status)
                || TaskInstanceConstant.CANCELED.equals(status)
                || TaskInstanceConstant.ABORTED.equals(status)) {
                UserTaskIndexDAO userTaskIndexDAO = (UserTaskIndexDAO) processEngineConfiguration
                    .getInstanceAccessor().access("userTaskIndexDAO");
                userTaskIndexDAO.deleteByTaskInstanceId(
                    Long.valueOf(taskInstance.getInstanceId()), taskInstance.getTenantId());
            }
        }

        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = TaskInstanceBuilder.buildTaskInstanceEntity(taskInstance);
        int result = taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);

        if (result > 0 && isShardingEnabled(processEngineConfiguration)) {
            String status = taskInstance.getStatus();
            if (TaskInstanceConstant.COMPLETED.equals(status)
                || TaskInstanceConstant.CANCELED.equals(status)
                || TaskInstanceConstant.ABORTED.equals(status)) {
                UserTaskIndexDAO userTaskIndexDAO = (UserTaskIndexDAO) processEngineConfiguration
                    .getInstanceAccessor().access("userTaskIndexDAO");
                userTaskIndexDAO.deleteByTaskInstanceId(
                    Long.valueOf(taskInstance.getInstanceId()), taskInstance.getTenantId());
            }
        }

        return result;
    }

    @Override
    public TaskInstance find(String instanceId,String tenantId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(Long.valueOf(instanceId),tenantId);
        if (taskInstanceEntity == null){
            return null;
        }
        return TaskInstanceBuilder.buildTaskInstanceFromEntity(taskInstanceEntity);
    }




    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        taskInstanceDAO.delete(Long.valueOf(instanceId),tenantId);

    }

    private List<TaskInstance> findTaskListByAssigneeFromIndex(TaskInstanceQueryByAssigneeParam param,
                                                               ProcessEngineConfiguration processEngineConfiguration) {
        UserTaskIndexDAO userTaskIndexDAO = (UserTaskIndexDAO) processEngineConfiguration
            .getInstanceAccessor().access("userTaskIndexDAO");
        List<UserTaskIndexEntity> indexEntities = userTaskIndexDAO.findByAssignee(param);

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(indexEntities.size());
        for (UserTaskIndexEntity indexEntity : indexEntities) {
            TaskInstance taskInstance = buildTaskInstanceFromIndex(indexEntity);
            taskInstanceList.add(taskInstance);
        }
        return taskInstanceList;
    }

    private TaskInstance buildTaskInstanceFromIndex(UserTaskIndexEntity indexEntity) {
        DefaultTaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(indexEntity.getTaskInstanceId().toString());
        taskInstance.setProcessInstanceId(indexEntity.getProcessInstanceId().toString());
        taskInstance.setProcessDefinitionType(indexEntity.getProcessDefinitionType());
        taskInstance.setDomainCode(indexEntity.getDomainCode());
        taskInstance.setExtra(indexEntity.getExtra());
        taskInstance.setStatus(indexEntity.getTaskStatus());
        taskInstance.setTitle(indexEntity.getTitle());
        taskInstance.setPriority(indexEntity.getPriority());
        taskInstance.setTenantId(indexEntity.getTenantId());
        // Note: some fields like activityInstanceId, executionInstanceId are not in the index table.
        // Callers that need these fields must query the main table by processInstanceId + taskInstanceId.
        return taskInstance;
    }

    private boolean isShardingEnabled(ProcessEngineConfiguration config) {
        if (config.getOptionContainer() == null) {
            return false;
        }
        ConfigurationOption option = config.getOptionContainer().get("shardingModeEnabled");
        return option != null && option.isEnabled();
    }
}

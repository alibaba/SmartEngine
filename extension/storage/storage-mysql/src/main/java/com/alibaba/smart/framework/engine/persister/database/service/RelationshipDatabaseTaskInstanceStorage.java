package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.builder.TaskInstanceBuilder;
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
            Long.valueOf(processInstanceId),taskInstanceQueryParam.getStatus(),taskInstanceQueryParam.getTenantId());

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


        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = TaskInstanceBuilder.buildTaskInstanceEntity(taskInstance);
        return taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);
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
}

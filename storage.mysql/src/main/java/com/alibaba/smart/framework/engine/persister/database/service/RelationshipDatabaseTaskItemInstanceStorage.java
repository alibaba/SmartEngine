package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskItemInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskItemInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskItemInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskItemQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryByAssigneeParam;

public class RelationshipDatabaseTaskItemInstanceStorage implements TaskItemInstanceStorage {

    @Override
    public List<TaskItemInstance> findPendingTaskItemList(PendingTaskItemQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return findTaskItemListByAssignee(convertToTaskItemInstanceQueryByAssigneeParam(pendingTaskQueryParam), processEngineConfiguration);
    }



    private TaskItemInstanceQueryByAssigneeParam convertToTaskItemInstanceQueryByAssigneeParam(
        PendingTaskItemQueryParam pendingTaskQueryParam) {
        TaskItemInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskItemInstanceQueryByAssigneeParam();
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
    public Long countPendingTaskItemList(PendingTaskItemQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return countTaskItemListByAssignee(convertToTaskItemInstanceQueryByAssigneeParam(pendingTaskQueryParam),processEngineConfiguration );
    }

    @Override
    public List<TaskItemInstance> findTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskItemInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskItemByAssignee(param);
        List<TaskItemInstance> taskInstanceList = new ArrayList<TaskItemInstance>(taskInstanceEntityList.size());
        for (TaskItemInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskItemInstance taskInstance= buildTaskItemInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }
        return taskInstanceList;
    }

    @Override
    public Long countTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.countTaskItemByAssignee(param);
        return  count  == null? 0L:count;
    }

    @Override
    public List<TaskItemInstance> findTaskItemByProcessInstanceIdAndStatus(TaskItemInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        String processInstanceId = taskInstanceQueryParam.getProcessInstanceIdList().get(0);
        List<TaskItemInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskItemByProcessInstanceIdAndStatus(
            Long.valueOf(processInstanceId),taskInstanceQueryParam.getStatus());

        List<TaskItemInstance> taskInstanceList = new ArrayList<TaskItemInstance>(taskInstanceEntityList.size());
        for (TaskItemInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskItemInstance taskInstance= buildTaskItemInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }



    @Override
    public List<TaskItemInstance> findTaskList(TaskItemInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskItemInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskItemList(taskInstanceQueryParam);

        List<TaskItemInstance> taskInstanceList = new ArrayList<TaskItemInstance>(taskInstanceEntityList.size());
        for (TaskItemInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

          TaskItemInstance taskInstance= buildTaskItemInstanceFromEntity(taskInstanceEntity);

          taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }

    @Override
    public Long count(TaskItemInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.count(taskInstanceQueryParam);
        return  count  == null? 0L:count;
    }

    //@Override
    //public List<TaskItemInstance> findList(Long processInstanceId, Long activityInstanceId) {
    //    TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
    //    List<TaskItemInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findList(processInstanceId ,activityInstanceId);
    //
    //    List<TaskItemInstance> taskInstanceList = new ArrayList<TaskItemInstance>(taskInstanceEntityList.size());
    //    for (TaskItemInstanceEntity taskInstanceEntity : taskInstanceEntityList) {
    //            TaskItemInstance taskInstance= buildTaskItemInstance(taskInstanceEntity);
    //            taskInstanceList.add(taskInstance);
    //
    //    }
    //
    //    return taskInstanceList;
    //}

    @Override
    public TaskItemInstance insert(TaskItemInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");

        TaskItemInstanceEntity taskInstanceEntity = buildTaskItemInstanceEntity(taskInstance);
        taskInstanceDAO.insert(taskInstanceEntity);

        Long entityId = taskInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf( taskInstance.getInstanceId());
        }

        taskInstanceEntity = taskInstanceDAO.findOne(entityId);

        //reAssign
        TaskItemInstance   resultTaskItemInstance= buildTaskItemInstanceFromEntity(taskInstanceEntity);
        resultTaskItemInstance.setTaskItemAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());

        return resultTaskItemInstance;
    }


    @Override
    public TaskItemInstance update(TaskItemInstance taskInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskItemInstanceEntity taskInstanceEntity = buildTaskItemInstanceEntity(taskInstance);
        taskInstanceDAO.update(taskInstanceEntity);


        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskItemInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskItemInstanceEntity taskInstanceEntity = buildTaskItemInstanceEntity(taskInstance);
        return taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);
    }

    @Override
    public TaskItemInstance find(String instanceId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskItemInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(Long.valueOf(instanceId));

        TaskItemInstance taskInstance= buildTaskItemInstanceFromEntity(taskInstanceEntity);
        return  taskInstance;
    }

    private TaskItemInstance buildTaskItemInstanceFromEntity(TaskItemInstanceEntity taskInstanceEntity) {
        TaskItemInstance taskInstance = new DefaultTaskItemInstance();
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
        return taskInstance;
    }

    private TaskItemInstanceEntity buildTaskItemInstanceEntity(TaskItemInstance taskInstance) {
        TaskItemInstanceEntity taskInstanceEntity = new TaskItemInstanceEntity();

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
        TaskItemInstanceDAO taskInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        taskInstanceDAO.delete(Long.valueOf(instanceId));

    }
}

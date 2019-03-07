package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskItemInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskItemInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskItemInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryByAssigneeParam;

public class RelationshipDatabaseTaskItemInstanceStorage implements TaskItemInstanceStorage {

    /*@Override
    public List<TaskItemInstance> findPendingTaskItemList(PendingTaskItemQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return findTaskItemListByAssignee(convertToTaskItemInstanceQueryByAssigneeParam(pendingTaskQueryParam), processEngineConfiguration);
    }*/



    /*private TaskItemInstanceQueryByAssigneeParam convertToTaskItemInstanceQueryByAssigneeParam(
        PendingTaskItemQueryParam pendingTaskQueryParam) {
        TaskItemInstanceQueryByAssigneeParam taskItemInstanceQueryByAssigneeParam = new TaskItemInstanceQueryByAssigneeParam();
        taskItemInstanceQueryByAssigneeParam.setAssigneeGroupIdList(pendingTaskQueryParam.getAssigneeGroupIdList());
        taskItemInstanceQueryByAssigneeParam.setAssigneeUserId(pendingTaskQueryParam.getAssigneeUserId());
        taskItemInstanceQueryByAssigneeParam.setProcessDefinitionType(pendingTaskQueryParam.getProcessDefinitionType());

        List<String> processInstanceIdList = pendingTaskQueryParam.getProcessInstanceIdList();
        if(null != processInstanceIdList){
            List<Long> processInstanceIdList1  = new ArrayList<Long>(processInstanceIdList.size());
            for (String s : processInstanceIdList) {
                processInstanceIdList1.add(Long.valueOf(s));
            }
            taskItemInstanceQueryByAssigneeParam.setProcessInstanceIdList(processInstanceIdList1);
        }

        taskItemInstanceQueryByAssigneeParam.setPageOffset(pendingTaskQueryParam.getPageOffset());
        taskItemInstanceQueryByAssigneeParam.setPageSize(pendingTaskQueryParam.getPageSize());

        taskItemInstanceQueryByAssigneeParam.setStatus(taskInstanceConstant.PENDING);
        return taskItemInstanceQueryByAssigneeParam;
    }*/

    /*@Override
    public Long countPendingTaskItemList(PendingTaskItemQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return countTaskItemListByAssignee(convertToTaskItemInstanceQueryByAssigneeParam(pendingTaskQueryParam),processEngineConfiguration );
    }*/

    /*@Override
    public List<TaskItemInstance> findTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        List<TaskItemInstanceEntity>  taskItemInstanceEntityList= taskItemInstanceDAO.findTaskItemByAssignee(param);
        List<TaskItemInstance> taskItemInstanceList = new ArrayList<TaskItemInstance>(taskItemInstanceEntityList.size());
        for (TaskItemInstanceEntity taskItemInstanceEntity : taskItemInstanceEntityList) {

            TaskItemInstance taskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);

            taskItemInstanceList.add(taskItemInstance);

        }
        return taskItemInstanceList;
    }

    @Override
    public Long countTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        Integer count = taskItemInstanceDAO.countTaskItemByAssignee(param);
        return  count  == null? 0L:count;
    }*/

    @Override
    public List<TaskItemInstance> findTaskItemByProcessInstanceIdAndStatus(TaskItemInstanceQueryParam taskItemInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        String processInstanceId = taskItemInstanceQueryParam.getProcessInstanceIdList().get(0);
        List<TaskItemInstanceEntity>  taskItemInstanceEntityList= taskItemInstanceDAO.findTaskItemByProcessInstanceIdAndStatus(
            Long.valueOf(processInstanceId),taskItemInstanceQueryParam.getStatus());

        List<TaskItemInstance> taskItemInstanceList = new ArrayList<TaskItemInstance>(taskItemInstanceEntityList.size());
        for (TaskItemInstanceEntity taskItemInstanceEntity : taskItemInstanceEntityList) {

            TaskItemInstance taskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);

            taskItemInstanceList.add(taskItemInstance);

        }

        return taskItemInstanceList;
    }



    @Override
    public List<TaskItemInstance> findTaskItemList(TaskItemInstanceQueryParam taskItemInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        List<TaskItemInstanceEntity>  taskItemInstanceEntityList= taskItemInstanceDAO.findTaskItemList(taskItemInstanceQueryParam);

        List<TaskItemInstance> taskItemInstanceList = new ArrayList<TaskItemInstance>(taskItemInstanceEntityList.size());
        for (TaskItemInstanceEntity taskItemInstanceEntity : taskItemInstanceEntityList) {

          TaskItemInstance taskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);

          taskItemInstanceList.add(taskItemInstance);

        }

        return taskItemInstanceList;
    }

    @Override
    public Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        Integer count = taskItemInstanceDAO.count(taskItemInstanceQueryParam);
        return  count  == null? 0L:count;
    }

    @Override
    public TaskItemInstance insert(TaskItemInstance taskItemInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");

        TaskItemInstanceEntity taskItemInstanceEntity = buildTaskItemInstanceEntity(taskItemInstance);
        taskItemInstanceDAO.insert(taskItemInstanceEntity);

        Long entityId = taskItemInstanceEntity.getId();

        // 当数据库表id 是非自增时，需要以传入的 id 值为准
        if(0L == entityId){
            entityId = Long.valueOf(taskItemInstance.getInstanceId());
        }

        taskItemInstanceEntity = taskItemInstanceDAO.findOne(entityId);
        //reAssign
        TaskItemInstance resultTaskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);
        return resultTaskItemInstance;
    }


    @Override
    public TaskItemInstance update(TaskItemInstance taskItemInstance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        TaskItemInstanceEntity taskItemInstanceEntity = buildTaskItemInstanceEntity(taskItemInstance);
        taskItemInstanceDAO.update(taskItemInstanceEntity);


        return taskItemInstance;
    }

    @Override
    public int updateFromStatus(TaskItemInstance taskItemInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        TaskItemInstanceEntity taskItemInstanceEntity = buildTaskItemInstanceEntity(taskItemInstance);
        return taskItemInstanceDAO.updateFromStatus(taskItemInstanceEntity,fromStatus);
    }

    @Override
    public TaskItemInstance find(String instanceId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        TaskItemInstanceEntity taskItemInstanceEntity =  taskItemInstanceDAO.findOne(Long.valueOf(instanceId));

        TaskItemInstance taskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);
        return  taskItemInstance;
    }

    @Override
    public TaskItemInstance find(String taskInstanceId, String subBizId,
                                 ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        TaskItemInstanceEntity taskItemInstanceEntity =  taskItemInstanceDAO.find(taskInstanceId, subBizId);

        TaskItemInstance taskItemInstance= buildTaskItemInstanceFromEntity(taskItemInstanceEntity);
        return  taskItemInstance;
    }

    private TaskItemInstance buildTaskItemInstanceFromEntity(TaskItemInstanceEntity taskItemInstanceEntity) {
        TaskItemInstance taskItemInstance = new DefaultTaskItemInstance();
        taskItemInstance.setInstanceId(taskItemInstanceEntity.getId().toString());
        taskItemInstance.setStartTime(taskItemInstanceEntity.getGmtCreate());
        taskItemInstance.setProcessDefinitionIdAndVersion(taskItemInstanceEntity.getProcessDefinitionIdAndVersion());
        taskItemInstance.setProcessInstanceId(taskItemInstanceEntity.getProcessInstanceId().toString());
        taskItemInstance.setActivityInstanceId(taskItemInstanceEntity.getActivityInstanceId().toString());
        taskItemInstance.setProcessDefinitionType(taskItemInstanceEntity.getProcessDefinitionType());
        taskItemInstance.setTag(taskItemInstanceEntity.getTag());
        taskItemInstance.setStatus(taskItemInstanceEntity.getStatus());

        taskItemInstance.setProcessDefinitionActivityId(taskItemInstanceEntity.getProcessDefinitionActivityId());
        taskItemInstance.setExecutionInstanceId(taskItemInstanceEntity.getExecutionInstanceId().toString());

        taskItemInstance.setClaimUserId(taskItemInstanceEntity.getClaimUserId());
        taskItemInstance.setCompleteTime(taskItemInstanceEntity.getCompleteTime());
        taskItemInstance.setClaimTime(taskItemInstanceEntity.getClaimTime());
        taskItemInstance.setComment(taskItemInstanceEntity.getComment());
        taskItemInstance.setExtension(taskItemInstanceEntity.getExtension());
        taskItemInstance.setTitle(taskItemInstanceEntity.getTitle());
        taskItemInstance.setBizId(taskItemInstanceEntity.getBizId());
        taskItemInstance.setSubBizId(taskItemInstanceEntity.getSubBizId());
        taskItemInstance.setTaskInstanceId(taskItemInstanceEntity.getTaskInstanceId());
        return taskItemInstance;
    }

    private TaskItemInstanceEntity buildTaskItemInstanceEntity(TaskItemInstance taskItemInstance) {
        TaskItemInstanceEntity taskItemInstanceEntity = new TaskItemInstanceEntity();

        taskItemInstanceEntity.setId(Long.valueOf(taskItemInstance.getInstanceId()));
        taskItemInstanceEntity.setProcessDefinitionIdAndVersion(taskItemInstance.getProcessDefinitionIdAndVersion());
        taskItemInstanceEntity.setProcessInstanceId(Long.valueOf(taskItemInstance.getProcessInstanceId()));
        taskItemInstanceEntity.setActivityInstanceId(Long.valueOf(taskItemInstance.getActivityInstanceId()));
        taskItemInstanceEntity.setProcessDefinitionActivityId(taskItemInstance.getProcessDefinitionActivityId());
        taskItemInstanceEntity.setExecutionInstanceId(Long.valueOf(taskItemInstance.getExecutionInstanceId()));
        taskItemInstanceEntity.setClaimUserId(taskItemInstance.getClaimUserId());
        taskItemInstanceEntity.setClaimTime(taskItemInstance.getClaimTime());
        taskItemInstanceEntity.setStatus(taskItemInstance.getStatus());
        taskItemInstanceEntity.setCompleteTime(taskItemInstance.getCompleteTime());
        taskItemInstanceEntity.setPriority(taskItemInstance.getPriority());
        taskItemInstanceEntity.setTag(taskItemInstance.getTag());
        taskItemInstanceEntity.setProcessDefinitionType(taskItemInstance.getProcessDefinitionType());
        taskItemInstanceEntity.setClaimTime(taskItemInstance.getClaimTime());
        taskItemInstanceEntity.setComment(taskItemInstance.getComment());
        taskItemInstanceEntity.setTitle(taskItemInstance.getTitle());
        taskItemInstanceEntity.setExtension(taskItemInstance.getExtension());
        taskItemInstanceEntity.setBizId(taskItemInstance.getBizId());
        taskItemInstanceEntity.setSubBizId(taskItemInstance.getSubBizId());
        taskItemInstanceEntity.setTaskInstanceId(taskItemInstance.getTaskInstanceId());
        //taskItemInstanceEntity.setGmtModified(taskItemInstance.getCompleteTime());
        return taskItemInstanceEntity;
    }


    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        TaskItemInstanceDAO taskItemInstanceDAO= (TaskItemInstanceDAO) SpringContextUtil.getBean("taskItemInstanceDAO");
        taskItemInstanceDAO.delete(Long.valueOf(instanceId));

    }
}

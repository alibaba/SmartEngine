package com.alibaba.smart.framework.engine.persister.database.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import org.springframework.beans.BeanUtils;


public class RelationshipDatabaseTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        return findTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam));
    }

    private TaskInstanceQueryByAssigneeParam convertToTaskInstanceQueryByAssigneeParam(PendingTaskQueryParam pendingTaskQueryParam) {
        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        BeanUtils.copyProperties(pendingTaskQueryParam, taskInstanceQueryByAssigneeParam);
        taskInstanceQueryByAssigneeParam.setStatus(TaskInstanceConstant.PENDING);
        return taskInstanceQueryByAssigneeParam;
    }

    @Override
    public Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        return countTaskListByAssignee(convertToTaskInstanceQueryByAssigneeParam(pendingTaskQueryParam));
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByAssignee(param);
        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }
        return taskInstanceList;
    }

    @Override
    public Integer countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.countTaskByAssignee(param);
        return  count  == null? 0:count;
    }

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskByProcessInstanceIdAndStatus(taskInstanceQueryParam.getProcessInstanceIdList().get(0),taskInstanceQueryParam.getStatus());

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

            TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

            taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }



    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        List<TaskInstanceEntity>  taskInstanceEntityList= taskInstanceDAO.findTaskList(taskInstanceQueryParam);

        List<TaskInstance> taskInstanceList = new ArrayList<TaskInstance>(taskInstanceEntityList.size());
        for (TaskInstanceEntity taskInstanceEntity : taskInstanceEntityList) {

          TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);

          taskInstanceList.add(taskInstance);

        }

        return taskInstanceList;
    }

    @Override
    public Integer count(TaskInstanceQueryParam taskInstanceQueryParam) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        Integer count = taskInstanceDAO.count(taskInstanceQueryParam);
        return  count  == null? 0:count;
    }

    //@Override
    //public List<TaskInstance> findList(Long processInstanceId, Long activityInstanceId) {
    //    TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
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
    public TaskInstance insert(TaskInstance taskInstance) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");

        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.insert(taskInstanceEntity);

        taskInstanceEntity = taskInstanceDAO.findOne(taskInstanceEntity.getId());

        //reAssign
        TaskInstance   resultTaskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        resultTaskInstance.setTaskAssigneeInstanceList(taskInstance.getTaskAssigneeInstanceList());

        return resultTaskInstance;
    }


    @Override
    public TaskInstance update(TaskInstance taskInstance) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        taskInstanceDAO.update(taskInstanceEntity);


        return taskInstance;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity = buildTaskInstanceEntity(taskInstance);
        return taskInstanceDAO.updateFromStatus(taskInstanceEntity,fromStatus);
    }

    @Override
    public TaskInstance find(Long instanceId) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        TaskInstanceEntity taskInstanceEntity =  taskInstanceDAO.findOne(instanceId);

        TaskInstance taskInstance= buildTaskInstanceFromEntity(taskInstanceEntity);
        return  taskInstance;
    }

    private TaskInstance buildTaskInstanceFromEntity(TaskInstanceEntity taskInstanceEntity) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(taskInstanceEntity.getId());
        taskInstance.setStartTime(taskInstanceEntity.getGmtCreate());
        taskInstance.setProcessDefinitionIdAndVersion(taskInstanceEntity.getProcessDefinitionIdAndVersion());
        taskInstance.setProcessInstanceId(taskInstanceEntity.getProcessInstanceId());
        taskInstance.setActivityInstanceId(taskInstanceEntity.getActivityInstanceId());
        taskInstance.setProcessDefinitionType(taskInstanceEntity.getProcessDefinitionType());
        taskInstance.setTag(taskInstanceEntity.getTag());
        taskInstance.setStatus(taskInstanceEntity.getStatus());

        taskInstance.setProcessDefinitionActivityId(taskInstanceEntity.getProcessDefinitionActivityId());
        taskInstance.setExecutionInstanceId(taskInstanceEntity.getExecutionInstanceId());

        taskInstance.setClaimUserId(taskInstanceEntity.getClaimUserId());
        taskInstance.setCompleteTime(taskInstanceEntity.getCompleteTime());
        taskInstance.setClaimTime(taskInstanceEntity.getClaimTime());

        return taskInstance;
    }

    private TaskInstanceEntity buildTaskInstanceEntity(TaskInstance taskInstance) {
        TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();

        taskInstanceEntity.setId(taskInstance.getInstanceId());
        taskInstanceEntity.setProcessDefinitionIdAndVersion(taskInstance.getProcessDefinitionIdAndVersion());
        taskInstanceEntity.setProcessInstanceId(taskInstance.getProcessInstanceId());
        taskInstanceEntity.setActivityInstanceId(taskInstance.getActivityInstanceId());
        taskInstanceEntity.setProcessDefinitionActivityId(taskInstance.getProcessDefinitionActivityId());
        taskInstanceEntity.setExecutionInstanceId(taskInstance.getExecutionInstanceId());
        taskInstanceEntity.setClaimUserId(taskInstance.getClaimUserId());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        taskInstanceEntity.setStatus(taskInstance.getStatus());
        taskInstanceEntity.setCompleteTime(taskInstance.getCompleteTime());
        taskInstanceEntity.setPriority(taskInstance.getPriority());
        taskInstanceEntity.setTag(taskInstance.getTag());
        taskInstanceEntity.setProcessDefinitionType(taskInstance.getProcessDefinitionType());
        taskInstanceEntity.setClaimTime(taskInstance.getClaimTime());
        //taskInstanceEntity.setGmtModified(taskInstance.getCompleteTime());
        return taskInstanceEntity;
    }


    @Override
    public void remove(Long instanceId) {
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        taskInstanceDAO.delete(instanceId);

    }
}

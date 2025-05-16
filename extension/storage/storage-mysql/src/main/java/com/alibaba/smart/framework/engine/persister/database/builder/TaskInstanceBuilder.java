package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class TaskInstanceBuilder {
    public static TaskInstanceQueryByAssigneeParam convertToTaskInstanceQueryByAssigneeParam(PendingTaskQueryParam pendingTaskQueryParam) {
        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        taskInstanceQueryByAssigneeParam.setAssigneeGroupIdList(pendingTaskQueryParam.getAssigneeGroupIdList());
        taskInstanceQueryByAssigneeParam.setAssigneeUserId(pendingTaskQueryParam.getAssigneeUserId());
        taskInstanceQueryByAssigneeParam.setProcessDefinitionType(pendingTaskQueryParam.getProcessDefinitionType());

        List<String> processInstanceIdList = pendingTaskQueryParam.getProcessInstanceIdList();
        if(null != processInstanceIdList){
            List<Long> idArrayList  = new ArrayList<Long>(processInstanceIdList.size());
            for (String s : processInstanceIdList) {
                idArrayList.add(Long.valueOf(s));
            }
            taskInstanceQueryByAssigneeParam.setProcessInstanceIdList(idArrayList);
        }

        taskInstanceQueryByAssigneeParam.setPageOffset(pendingTaskQueryParam.getPageOffset());
        taskInstanceQueryByAssigneeParam.setPageSize(pendingTaskQueryParam.getPageSize());

        taskInstanceQueryByAssigneeParam.setStatus(TaskInstanceConstant.PENDING);
        taskInstanceQueryByAssigneeParam.setTenantId(pendingTaskQueryParam.getTenantId());
        return taskInstanceQueryByAssigneeParam;
    }

    public static TaskInstance buildTaskInstanceFromEntity(TaskInstanceEntity taskInstanceEntity) {
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
        taskInstance.setTenantId(taskInstanceEntity.getTenantId());

        return taskInstance;
    }

    public static TaskInstanceEntity buildTaskInstanceEntity(TaskInstance taskInstance) {
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
        taskInstanceEntity.setTenantId(taskInstance.getTenantId());
        //taskInstanceEntity.setGmtModified(taskInstance.getCompleteTime());

        Date currentDate = DateUtil.getCurrentDate();
        taskInstanceEntity.setGmtCreate(currentDate);
        taskInstanceEntity.setGmtModified(currentDate);

        return taskInstanceEntity;
    }
}

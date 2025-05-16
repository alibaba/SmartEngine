package com.alibaba.smart.framework.engine.persister.database.builder;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;

import java.util.Date;

/**
 * @author yanricheng@163.com
 * @date 2025/05/015
 */
public final class TaskAssigneeInstanceBuilder {

    public static TaskAssigneeEntity buildTaskInstanceEntity(TaskAssigneeInstance taskAssigneeInstance) {
        TaskAssigneeEntity taskAssigneeEntity = new TaskAssigneeEntity();

        taskAssigneeEntity.setProcessInstanceId(Long.valueOf(taskAssigneeInstance.getProcessInstanceId()));
        taskAssigneeEntity.setTaskInstanceId(Long.valueOf(taskAssigneeInstance.getTaskInstanceId()));
        taskAssigneeEntity.setAssigneeId(taskAssigneeInstance.getAssigneeId());
        taskAssigneeEntity.setAssigneeType(taskAssigneeInstance.getAssigneeType());
        String taskAssigneeInstanceId = taskAssigneeInstance.getInstanceId();
        if (null != taskAssigneeInstanceId) {
            taskAssigneeEntity.setId(Long.valueOf(taskAssigneeInstanceId));
        }

        Date currentDate = DateUtil.getCurrentDate();
        taskAssigneeEntity.setGmtCreate(currentDate);
        taskAssigneeEntity.setGmtModified(currentDate);
        taskAssigneeEntity.setTenantId(taskAssigneeInstance.getTenantId());


        return taskAssigneeEntity;
    }

    public static TaskAssigneeInstance buildTaskAssigneeInstance(TaskAssigneeEntity taskAssigneeEntity) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();

        taskAssigneeInstance.setInstanceId(taskAssigneeEntity.getId().toString());
        taskAssigneeInstance.setProcessInstanceId(taskAssigneeEntity.getProcessInstanceId().toString());
        taskAssigneeInstance.setTaskInstanceId(taskAssigneeEntity.getTaskInstanceId().toString());


        taskAssigneeInstance.setAssigneeId(taskAssigneeEntity.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeEntity.getAssigneeType());

        taskAssigneeInstance.setStartTime(taskAssigneeEntity.getGmtCreate());
        taskAssigneeInstance.setCompleteTime(taskAssigneeEntity.getGmtModified());
        taskAssigneeInstance.setTenantId(taskAssigneeEntity.getTenantId());

        return taskAssigneeInstance;
    }
}

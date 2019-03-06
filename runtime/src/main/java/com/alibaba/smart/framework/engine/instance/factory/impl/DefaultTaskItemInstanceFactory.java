package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TaskItemInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskItemInstance;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;

/**
 * 默认任务实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultTaskItemInstanceFactory implements TaskItemInstanceFactory {

    @Override
    public TaskItemInstance create(Activity activity, ExecutionInstance executionInstance, ExecutionContext context) {
        TaskItemInstance taskItemInstance = new DefaultTaskItemInstance();

        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
        taskItemInstance.setInstanceId(idGenerator.getId());
        taskItemInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskItemInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskItemInstance.setProcessDefinitionActivityId(activity.getId());
        taskItemInstance.setExecutionInstanceId(executionInstance.getInstanceId());
        taskItemInstance.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        taskItemInstance.setStartTime(DateUtil.getCurrentDate());
        taskItemInstance.setStatus(TaskInstanceConstant.PENDING);
        Map<String, Object> request = context.getRequest();
        if (null != request) {
            String title = (String)request.get(RequestMapSpecialKeyConstant.TASK_TITLE);
            taskItemInstance.setTitle(title);
        }
        ProcessInstance processInstance = context.getProcessInstance();
        taskItemInstance.setProcessDefinitionType(processInstance.getProcessDefinitionType());


        return taskItemInstance;
    }
}

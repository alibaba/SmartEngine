package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * 默认任务实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultTaskInstanceFactory implements TaskInstanceFactory {

    @Override
    public TaskInstance create(PvmActivity pvmActivity, ExecutionInstance executionInstance,ExecutionContext context) {
        TaskInstance taskInstance = new DefaultTaskInstance();

        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

        taskInstance.setInstanceId(idGenerator.getId());
        taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskInstance.setActivityId(pvmActivity.getModel().getId());
        taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());
        taskInstance.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        taskInstance.setStartDate(DateUtil.getCurrentDate());

        return taskInstance;
    }
}

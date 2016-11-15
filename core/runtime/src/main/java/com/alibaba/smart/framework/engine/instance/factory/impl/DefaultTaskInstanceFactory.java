package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.util.DateUtil;

/**
 * 默认任务实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultTaskInstanceFactory implements TaskInstanceFactory {

    @Override
    public TaskInstance create(PvmActivity pvmActivity, ExecutionInstance executionInstance) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(InstanceIdUtil.uuid());
        taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskInstance.setActivityId(pvmActivity.getModel().getId());
        taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());

        taskInstance.setStartDate(DateUtil.getCurrentDate());

        return taskInstance;
    }
}

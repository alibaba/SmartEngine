package com.alibaba.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.util.ObjUtil;

/**
 * 默认任务实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceFactory.class)

public class DefaultTaskInstanceFactory implements TaskInstanceFactory {

    @Override
    public TaskInstance create(Activity activity, ExecutionInstance executionInstance, ExecutionContext context) {
        TaskInstance taskInstance = new DefaultTaskInstance();

        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

        idGenerator.generate(taskInstance);
        taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskInstance.setProcessDefinitionActivityId(activity.getId());
        taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());
        taskInstance.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        taskInstance.setStartTime(DateUtil.getCurrentDate());
        taskInstance.setStatus(TaskInstanceConstant.PENDING);
        Map<String, Object> request = context.getRequest();
        if (null != request) {
            String title = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_TITLE));
            taskInstance.setTitle(title);
        }
        ProcessInstance processInstance = context.getProcessInstance();
        taskInstance.setProcessDefinitionType(processInstance.getProcessDefinitionType());


        return taskInstance;
    }
}

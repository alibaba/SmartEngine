package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> implements ActivityBehavior<UserTask> {

    public UserTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void execute(PvmActivity pvmActivity, ExecutionContext context) {
        //TODO 在父类控制
        context.setNeedPause(true);

        ProcessInstance processInstance = context.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, processInstance);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance);

        TaskInstance taskInstance = super.taskInstanceFactory.create(pvmActivity, executionInstance);

        executionInstance.setTaskInstance(taskInstance);
        activityInstance.setExecutionInstance(executionInstance);

        processInstance.addActivityInstance(activityInstance);
    }
}

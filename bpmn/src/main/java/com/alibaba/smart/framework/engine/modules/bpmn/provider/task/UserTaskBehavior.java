package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> {

    public UserTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public boolean enter(ExecutionContext context) {
        ExecutionInstance executionInstance=context.getExecutionInstance();
        TaskInstance taskInstance = super.taskInstanceFactory.create(this.getModel(), executionInstance, context);

        Map<String, Object> request = context.getRequest();
        if(null != request){

            //TODO 约定了key。。。
            String assigneeId = (String) request.get("assigneeId");
            if(null != assigneeId){
                taskInstance.setAssigneeId(assigneeId);
            }
        }

        executionInstance.setTaskInstance(taskInstance);
        return true;
    }
}

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

import java.util.Map;

public class UserTaskBehavior extends AbstractActivityBehavior<UserTask> implements ActivityBehavior<UserTask> {

    public UserTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext context) {

        ProcessInstance processInstance = context.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, context);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance,context);

        TaskInstance taskInstance = super.taskInstanceFactory.create(pvmActivity, executionInstance,  context);

        Map<String, Object> request = context.getRequest();
        if(null != request){

            //TODO 约定了key。。。
            String assigneeId = (String) request.get("assigneeId");
            if(null != assigneeId){
                taskInstance.setAssigneeId(assigneeId);
            }
        }


        executionInstance.setTaskInstance(taskInstance);
        activityInstance.setExecutionInstance(executionInstance);

        processInstance.addNewActivityInstance(activityInstance);

    }

    @Override
    public boolean needSuspend(ExecutionContext context) {
        return true;
    }


}

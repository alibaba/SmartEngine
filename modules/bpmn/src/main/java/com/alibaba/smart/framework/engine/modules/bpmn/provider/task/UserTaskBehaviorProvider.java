package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class UserTaskBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<UserTask> implements ActivityBehaviorProvider<UserTask> {

    public UserTaskBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public Invoker createCustomInvoker(PvmActivity runtimeActivity) {
        return new UserTaskInvoker(runtimeActivity);
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {

    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = ReceiveTask.class)
public class ReceiveTaskBehavior extends AbstractActivityBehavior<ReceiveTask> {

    //public ReceiveTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}

    public ReceiveTaskBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context) {

        super.enter(context);

        return true;
    }

}

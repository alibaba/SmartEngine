package com.alibaba.smart.framework.engine.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ReceiveTask.class)
public class ReceiveTaskBehavior extends AbstractActivityBehavior<ReceiveTask> {

    public ReceiveTaskBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        super.enter(context, pvmActivity);

        return true;
    }

}

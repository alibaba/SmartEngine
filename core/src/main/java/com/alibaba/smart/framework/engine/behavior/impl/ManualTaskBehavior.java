package com.alibaba.smart.framework.engine.behavior.impl;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.ManualTask;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;

import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ManualTask.class)
public class ManualTaskBehavior extends AbstractActivityBehavior<ManualTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualTaskBehavior.class);

    public ManualTaskBehavior() {
        super();
    }

    // If needed, override methods to add behavior specific to ManualTask
    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {
        // Manual task-specific logic here
        logActivityStart(pvmActivity.getModel().getId());
        return super.enter(context, pvmActivity);
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.task.util.TccDelegationUtil;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ReceiveTaskBehavior extends AbstractActivityBehavior<ReceiveTask> implements ActivityBehavior<ReceiveTask> {

    public ReceiveTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }


    @Override
    public void buildInstanceRelationShip(PvmActivity pvmActivity, ExecutionContext executionContext) {


        ProcessInstance processInstance = executionContext.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, executionContext);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance,  executionContext);

        activityInstance.setExecutionInstance(executionInstance);
        processInstance.addNewActivityInstance(activityInstance);

    }

    @Override
    public void leave(PvmActivity pvmActivity, ExecutionContext executionContext){
        ReceiveTask ReceiveTask = (ReceiveTask) pvmActivity.getModel();
        String className = ReceiveTask.getClassName();
        executeExtension(executionContext, className);
    }

    private void executeExtension(ExecutionContext executionContext, String className) {
        if (null == className) {

        } else {
            TccDelegationUtil.execute(executionContext, className);

        }
    }

    @Override
    public boolean needSuspend() {
        return true;
    }


}

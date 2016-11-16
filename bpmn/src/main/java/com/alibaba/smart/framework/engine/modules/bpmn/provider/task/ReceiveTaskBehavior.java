package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
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
        ActivityInstance activityInstance = super.activityInstanceFactory.create(pvmActivity, processInstance);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance);

        activityInstance.setExecutionInstance(executionInstance);
        processInstance.addActivityInstance(activityInstance);


        ReceiveTask ReceiveTask = (ReceiveTask) pvmActivity.getModel();
        String className = ReceiveTask.getClassName();
        executeExtension(executionContext, className);


    }

    private void executeExtension(ExecutionContext executionContext, String className) {
        //TODO
        if (null == className) {


        } else {
            // TODO need cache,rename
            Object ss = ClassLoaderUtil.createNewInstance(className);
            if (ss instanceof TccDelegation<?>) {
                TccDelegation<?> tccDelegation = (TccDelegation<?>) ss;
                TccResult<?> tccResult = tccDelegation.tryExecute(executionContext);
//                DefaultMessage defaultMessage = new DefaultMessage();
//                defaultMessage.setBody(tccResult);
//                return defaultMessage;
            }
        }
    }

    @Override
    public boolean needSuspend() {
        return true;
    }
}

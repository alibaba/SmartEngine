package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.util.ClassLoaderUtil;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.impl.SuspendMessage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ReceiveTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.event.ProcessEventInvoker;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.task.action.SpringAction;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ReceiveTaskBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<ReceiveTask> implements ActivityBehaviorProvider<ReceiveTask> {

    public ReceiveTaskBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected Invoker createExecuteInvoker() {

        return null;
    }

    @Override
    public Invoker createCustomInvoker(PvmActivity runtimeActivity) {
       return null;
    }

    @Override
    protected Invoker createEventInvoker(String event) {
        return new  ProcessEventInvoker(getRuntimeActivity());
    }

    @Override
    protected Invoker createStartInvoker() {
        //FIXME
        return context -> new SuspendMessage();
    }

    @Override
    public void execute(PvmActivity pvmActivity,ExecutionContext executionContext) {
        //TODO 在父类控制
        executionContext.setNeedPause(true);

        ProcessInstance processInstance = executionContext.getProcessInstance();
        ActivityInstance activityInstance = super.activityInstanceFactory.create( pvmActivity,processInstance);

        ExecutionInstance executionInstance = super.executionInstanceFactory.create(activityInstance);

        activityInstance.setExecutionInstance(executionInstance);
        processInstance.addActivityInstance(activityInstance);


        ReceiveTask ReceiveTask = (ReceiveTask) pvmActivity.getModel();
        String className = ReceiveTask.getClassName();
        executeExtension(executionContext, className);


    }

    private void executeExtension(ExecutionContext executionContext, String className) {
        //TODO
        if(null == className){



        }else{
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
}

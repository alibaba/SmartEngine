package com.alibaba.smart.framework.engine.invocation.impl;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultActivityTransitionSelectInvoker implements Invoker {

    private RuntimeActivity        runtimeActivity;
    private ExtensionPointRegistry extensionPointRegistry;

    @Override
    public Message invoke(InstanceContext context) {
        ProcessInstance processInstance = context.getProcessInstance();
        ExecutionInstance executionInstance = context.getCurrentExecution();
        ActivityInstance currentActivityInstance = executionInstance.getActivity();
        Map<String, RuntimeTransition> outcomeTransitions = this.runtimeActivity.getOutcomeTransitions();
        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            for (Map.Entry<String, RuntimeTransition> transitionEntry : outcomeTransitions.entrySet()) {
                RuntimeTransition runtimeTransition = transitionEntry.getValue();
                runtimeTransition.getSource();
                runtimeTransition.getTarget();

                TransitionInstanceFactory transitionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                        TransitionInstanceFactory.class);
                ActivityInstanceFactory activityInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                        ActivityInstanceFactory.class);

                TransitionInstance transitionInstance = transitionInstanceFactory.create();
                transitionInstance.setTransitionId(runtimeTransition.getId());
                transitionInstance.setSourceActivityInstanceId(currentActivityInstance.getInstanceId());

                ActivityInstance activityInstance = activityInstanceFactory.create();
                activityInstance.setInstanceId(InstanceIdUtils.uuid());
                activityInstance.setActivityId(runtimeTransition.getTarget().getId());
                activityInstance.setProcessInstanceId(processInstance.getInstanceId());
                activityInstance.addIncomeTransition(transitionInstance);

                //this.executionInstanceManager.updateActivity(processInstance.getInstanceId(), executionInstance.getInstanceId(),
                //                                             activityInstance);
                executionInstance.setActivity(activityInstance);

            }
            List<ExecutionInstance> executions = new ArrayList<>();
            executions.add(executionInstance);
            Message message = new DefaultMessage();
            message.setBody(executions);

            return message;
        }
        executionInstance.setStatus("completed");//TODO ettear
        executionInstance.setCompleteDate(new Date());
        //this.executionInstanceManager.complete(processInstance.getInstanceId(), executionInstance.getInstanceId());
        return null;
    }

    public RuntimeActivity getRuntimeActivity() {
        return runtimeActivity;
    }

    public void setRuntimeActivity(RuntimeActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }

    public ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

    public void setExtensionPointRegistry(
            ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }
}

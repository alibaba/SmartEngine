package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * Created by ettear on 16-5-4.
 */
public abstract class AbstractTransitionSelectInvoker implements Invoker {

    private ExtensionPointRegistry extensionPointRegistry;
    private RuntimeActivity        runtimeActivity;

    public AbstractTransitionSelectInvoker(ExtensionPointRegistry extensionPointRegistry,RuntimeActivity runtimeActivity) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.runtimeActivity = runtimeActivity;
    }

    @Override
    public Message invoke(InstanceContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        Map<String, RuntimeTransition> outcomeTransitions = this.runtimeActivity.getOutcomeTransitions();
        Message message = new DefaultMessage();
        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<RuntimeTransition> hitTransitions = new ArrayList<>();
            for (Map.Entry<String, RuntimeTransition> transitionEntry : outcomeTransitions.entrySet()) {
                RuntimeTransition runtimeTransition = transitionEntry.getValue();
                //执行命中判断逻辑
                Message result = runtimeTransition.invoke(AtomicOperationEvent.TRANSITION_HIT.name(), context);
                if (null != result) {
                    Object resultBody = result.getBody();
                    if (null == resultBody || !(resultBody instanceof Boolean) || !((Boolean) resultBody)) {
                        //没有命中
                        continue;
                    }
                }//无执行结果为命中
                hitTransitions.add(runtimeTransition);
            }

            if (hitTransitions.isEmpty()) {
                executionInstance.setStatus(InstanceStatus.suspended);
                //TODO ettear Exception
                //message.setBody();
                message.setFault(true);
            } else {
                List<ExecutionInstance> executions = new ArrayList<>();
                executions.add(executionInstance);
                message.setBody(this.processExecution(hitTransitions,context.getProcessInstance(),executionInstance,executionInstance.getActivity()));
            }
        } else {//没有后续节点，结束执行实例
            executionInstance.setStatus(InstanceStatus.completed);
            executionInstance.setCompleteDate(new Date());
            //this.executionInstanceManager.complete(processInstance.getInstanceId(), executionInstance.getInstanceId());
        }
        return message;
    }

    protected abstract List<ExecutionInstance> processExecution(List<RuntimeTransition> transitions,ProcessInstance processInstance,ExecutionInstance currentExecutionInstance,
                                                                ActivityInstance currentActivityInstance);

    protected void buildExecutionInstance(RuntimeTransition runtimeTransition,ProcessInstance processInstance,ExecutionInstance executionInstance,
                                          ActivityInstance currentActivityInstance
                                          ) {
        TransitionInstanceFactory transitionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(
                TransitionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = this.extensionPointRegistry.getExtensionPoint(
                ActivityInstanceFactory.class);

        TransitionInstance transitionInstance = transitionInstanceFactory.create();
        transitionInstance.setTransitionId(runtimeTransition.getId());
        transitionInstance.setSourceActivityInstanceId(currentActivityInstance.getInstanceId());

        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setActivityId(runtimeTransition.getTarget().getId());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        activityInstance.addIncomeTransition(transitionInstance);

        //this.executionInstanceManager.updateActivity(processInstance.getInstanceId(), executionInstance.getInstanceId(),
        //                                             activityInstance);
        executionInstance.setActivity(activityInstance);
    }

    protected RuntimeActivity getRuntimeActivity() {
        return runtimeActivity;
    }

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }
}

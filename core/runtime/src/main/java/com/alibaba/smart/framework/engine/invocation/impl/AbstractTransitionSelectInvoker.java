package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;

/**
 * Created by ettear on 16-5-4.
 */
public abstract class AbstractTransitionSelectInvoker implements Invoker {

    private PvmActivity            runtimeActivity;

    public AbstractTransitionSelectInvoker( PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }

    @Override
    public Message invoke(ExecutionContext context) {
        ExecutionInstance executionInstance = context.getCurrentExecution();
        Map<String, PvmTransition> outcomeTransitions = this.runtimeActivity.getOutcomeTransitions();
        Message message = new DefaultMessage();
        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> hitTransitions = new ArrayList<>();
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition runtimeTransition = transitionEntry.getValue();
                // 执行命中判断逻辑
                Message result = runtimeTransition.fireEvent(PvmEventConstant.TRANSITION_HIT.name(), context);
                if (null != result) {
                    Object resultBody = result.getBody();
                    if (null == resultBody || !(resultBody instanceof Boolean) || !((Boolean) resultBody)) {
                        // 没有命中
                        continue;
                    }
                }// 无执行结果为命中
                hitTransitions.add(runtimeTransition);
            }

            if (hitTransitions.isEmpty()) {
                executionInstance.setStatus(InstanceStatus.suspended);
                // TODO ettear Exception
                // message.setBody();
                message.setFault(true);
            } else {
                List<ExecutionInstance> executions = new ArrayList<>();
                executions.add(executionInstance);
                List<ExecutionInstance> processExecution = this.processExecution(hitTransitions, context.getProcessInstance(), executionInstance,
                                                      executionInstance.getActivity());
                message.setBody(processExecution);
            }
        } else {// 没有后续节点，结束执行实例
            executionInstance.setStatus(InstanceStatus.completed);
            executionInstance.setCompleteDate(new Date());
            // this.executionInstanceManager.complete(processInstance.getInstanceId(),
            // executionInstance.getInstanceId());
        }
        return message;
    }

    protected abstract List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
                                                                ProcessInstance processInstance,
                                                                ExecutionInstance currentExecutionInstance,
                                                                ActivityInstance currentActivityInstance);

    protected void buildExecutionInstance(PvmTransition runtimeTransition, ProcessInstance processInstance,
                                          ExecutionInstance executionInstance, ActivityInstance currentActivityInstance) {
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
        TransitionInstanceFactory transitionInstanceFactory = extensionPointRegistry.getExtensionPoint(TransitionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);

        TransitionInstance transitionInstance = transitionInstanceFactory.create();
        transitionInstance.setTransitionId(runtimeTransition.getId());
        transitionInstance.setSourceActivityInstanceId(currentActivityInstance.getInstanceId());

        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setActivityId(runtimeTransition.getTarget().getId());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        activityInstance.addIncomeTransition(transitionInstance);

        // this.executionInstanceManager.updateActivity(processInstance.getInstanceId(),
        // executionInstance.getInstanceId(),
        // activityInstance);
        executionInstance.setActivity(activityInstance);
    }

    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }
 
}

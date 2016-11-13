package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractGatewayInvoker implements Invoker {

    private PvmActivity            runtimeActivity;

    public AbstractGatewayInvoker( PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }

    @Override
    public Message invoke(ExecutionContext context) {
//        ExecutionInstance executionInstance = context.getCurrentExecution();
        Map<String, PvmTransition> outcomeTransitions = this.runtimeActivity.getOutcomeTransitions();
        Message message = new DefaultMessage();
        if (null != outcomeTransitions && !outcomeTransitions.isEmpty()) {
            List<PvmTransition> hitTransitions = new ArrayList<>();
            for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {
                PvmTransition pvmTransition = transitionEntry.getValue();
                 // 执行命中判断逻辑,执行 MvelInvoker
                 pvmTransition.fireEvent(PvmEventConstant.TRANSITION_HIT.name(), context);

                //FIXME
//                if (null != result) {
//                    Object resultBody = result.getBody();

//                        continue;

                // 无执行结果为命中
                hitTransitions.add(pvmTransition);
            }

            if (hitTransitions.isEmpty()) {
                throw new EngineException("No default outgoing transition found, check the  transition condition expression or specify the default transition.");
//                executionInstance.setStatus(InstanceStatus.suspended);
                // message.setBody();
//                message.setFault(true);
            } else {
                List<ExecutionInstance> executions = new ArrayList<>();
//                executions.add(executionInstance);
                List<ExecutionInstance> processExecution = null;
//                this.processExecution(hitTransitions, context.getProcessInstance()
//                                                      );
                message.setBody(processExecution);
            }
        } else {// 没有后续节点，结束执行实例
//            executionInstance.setStatus(InstanceStatus.completed);
//            executionInstance.setCompleteDate(new Date());
            // this.executionInstanceManager.complete(processInstance.getProcessId(),
            // executionInstance.getProcessId());
        }
        return message;
    }

    protected abstract List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
                                                                ProcessInstance processInstance,
                                                                ExecutionInstance currentExecutionInstance
                                                                );

    protected void buildExecutionInstance(PvmTransition runtimeTransition, ProcessInstance processInstance,
                                          ExecutionInstance executionInstance) {
//        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
//        TransitionInstanceFactory transitionInstanceFactory = extensionPointRegistry.getExtensionPoint(TransitionInstanceFactory.class);
//        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
//
//        TransitionInstance transitionInstance = transitionInstanceFactory.create();
//        transitionInstance.setTransitionId(runtimeTransition.getModel().getId());
//        //TODO
//       // transitionInstance.setSourceActivityInstanceId(currentActivityInstance.getInstanceId());
//
//        ActivityInstance activityInstance = activityInstanceFactory.create();
//        activityInstance.setActivityId(runtimeTransition.getTarget().getModel().getId());
//        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
//        activityInstance.addIncomeTransition(transitionInstance);
//
//        executionInstance.setActivityId(runtimeTransition.getTarget().getModel().getId());
    }

    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }
 
}

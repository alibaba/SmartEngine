package com.alibaba.smart.framework.engine.pvm.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.invocation.signal.Signal;
import com.alibaba.smart.framework.engine.model.instance.*;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;

import java.util.List;
import java.util.Map;


public class DefaultPvmProcessInstance implements PvmProcessInstance{

    @Override
    public void start(ExecutionContext executionContext) {
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();

        
        // 从扩展注册机获取实例工厂
        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        // InstanceFactFactory factFactory =
        // this.getExtensionPointRegistry().getExtensionPoint(InstanceFactFactory.class);
         PvmProcessDefinition pvmProcessDefinition=     executionContext.getPvmProcessDefinition();
        // 流程实例ID
        ProcessInstance processInstance = executionContext.getProcessInstance();
        String processInstanceId = processInstance.getInstanceId();
        // 构建活动实例: 指向开始节点
        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setProcessInstanceId(processInstanceId);
        PvmActivity startActivity = pvmProcessDefinition.getStartActivity();
        String startActivityId = startActivity.getModel().getId();
        
        activityInstance.setActivityId(startActivityId);
        // 构建执行实例
        ExecutionInstance executionInstance = executionContext.getCurrentExecution();
        
        executionInstance.setActivity(activityInstance);

        // 状态
        processInstance.setStatus(InstanceStatus.running);

        this.runProcess(startActivity, executionContext);
    }

    @Override
    public void run(ExecutionContext context) {


        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
        ProcessInstance processInstance = context.getProcessInstance();
        String processInstanceId = processInstance.getInstanceId();
        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setProcessInstanceId(processInstanceId);
        processInstance.setStatus(InstanceStatus.running);
        activityInstance.setCurrentStep(String.valueOf(PvmEventConstant.ACTIVITY_START.getCode()));
        activityInstance.setActivityId(context.getCurrentExecution().getActivity().getActivityId());
        context.getCurrentExecution().setActivity(activityInstance);
        PvmProcessDefinition pvmProcessDefinition=     context.getPvmProcessDefinition();
        Map<String,PvmActivity> activityMap = pvmProcessDefinition.getActivities();
        if (activityMap.isEmpty() || !activityMap.containsKey(context.getCurrentExecution().getActivity().getActivityId())) {
            throw new EngineException("not have this activity!");
        }
        try {

            this.runProcess(activityMap.get(context.getCurrentExecution().getActivity().getActivityId()),context);

        }catch (Signal signal) {
            context.getCurrentExecution().abort();
        }

    }

    private Message runProcess(PvmActivity startActivity, ExecutionContext context) {
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();


        Message   processMessage = this.executeActivity(startActivity, context);

        ProcessInstance processInstance = context.getProcessInstance();
        if (!processMessage.isSuspend()) {
            processInstance.setStatus(InstanceStatus.completed);
        } else {
            processInstance.setStatus(InstanceStatus.suspended);
        }
        //TODO 这边怎么突然来了一个存储 存储流程实例
        ProcessInstanceStorage processInstanceStorage = extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        processInstanceStorage.save(context.getProcessInstance());
        return processMessage;
    }
    
    private Message executeActivity(PvmActivity pvmActivity, ExecutionContext context) {
        // 执行当前节点

        Message activityExecuteMessage = pvmActivity.execute(context);


        Message processMessage = new DefaultMessage();
        if (activityExecuteMessage.isSuspend()) {
            processMessage.setSuspend(true);
            return processMessage;
        }

        // 执行后续节点选择
        Message transitionSelectMessage = pvmActivity.fireEvent(PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name(),
                                                                 context);
        if (null != transitionSelectMessage) {
            Object transitionSelectBody = transitionSelectMessage.getBody();
            if (null != transitionSelectBody && transitionSelectBody instanceof List) {
                List<?> executionObjects = (List<?>) transitionSelectBody;
                if (!executionObjects.isEmpty()) {
                    for (Object executionObject : executionObjects) {
                        // 执行所有实例
                        if (executionObject instanceof ExecutionInstance) {
                            ExecutionInstance executionInstance = (ExecutionInstance) executionObject;
                            context.setCurrentExecution(executionInstance);

                            TransitionInstance transitionInstance = executionInstance.getActivity().getIncomeTransitions().get(0);
                            PvmTransition pvmTransition = pvmActivity.getOutcomeTransitions().get(transitionInstance.getTransitionId());
                            pvmTransition.execute(context);
                            PvmActivity targetPvmActivity = pvmTransition.getTarget();
                            this.executeActivity(targetPvmActivity, context);
                        }else{
                            throw new EngineException("unpported class type:ExecutionInstance");
                        }
                    }
                    Map<String, ExecutionInstance> executionInstances = context.getProcessInstance().getExecutions();
                    if (null != executionInstances && executionInstances.isEmpty()) {
                        for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : executionInstances.entrySet()) {
                            ExecutionInstance executionInstance = executionInstanceEntry.getValue();
                            if (executionInstance.isSuspend()) {// 存在暂停的执行实例
                                processMessage.setSuspend(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return processMessage;
    }


}

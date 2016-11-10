package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.util.ThreadLocalUtil;


public class DefaultPvmProcessInstance implements PvmProcessInstance{

    @Override
    public void start(ExecutionContext executionContext) {
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();

        // TODO 实例的值赋值可否在固定的地方去完成组装?
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
        PvmActivity pvmStartActivity = pvmProcessDefinition.getStartActivity();
        String startActivityId = pvmStartActivity.getModel().getId();
        
        activityInstance.setActivityId(startActivityId);
        // 构建执行实例
        ExecutionInstance executionInstance = executionContext.getCurrentExecution();
        
        executionInstance.setActivity(activityInstance);

        // 状态
        processInstance.setStatus(InstanceStatus.running);

        //TODO 触发流程启动事件
//        this.fireEvent(PvmEventConstant.PROCESS_START.name(), executionContext);
        // 从开始节点开始执行
          this.startProcessInstance(pvmStartActivity, executionContext);
    }

//    @Override
//    public void run(ExecutionContext context) {
//        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();
//        ActivityInstanceFactory activityInstanceFactory = extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
//        ProcessInstance processInstance = context.getProcessInstance();
//        String processInstanceId = processInstance.getInstanceId();
//        ActivityInstance activityInstance = activityInstanceFactory.create();
//        activityInstance.setProcessInstanceId(processInstanceId);
//        processInstance.setStatus(InstanceStatus.running);
//        activityInstance.setCurrentStep(String.valueOf(PvmEventConstant.ACTIVITY_START.getCode()));
//        activityInstance.setActivityId(context.getCurrentExecution().getActivity().getActivityId());
//        context.getCurrentExecution().setActivity(activityInstance);
//        PvmProcessDefinition pvmProcessDefinition=     context.getPvmProcessDefinition();
//        Map<String,PvmActivity> activityMap = pvmProcessDefinition.getActivities();
//        if (activityMap.isEmpty() || !activityMap.containsKey(context.getCurrentExecution().getActivity().getActivityId())) {
//            throw new EngineException("not have this activity!");
//        }
//        this.startProcessInstance(activityMap.get(context.getCurrentExecution().getActivity().getActivityId()),context);
//
//
//    }

    private Message startProcessInstance(PvmActivity startActivity, ExecutionContext context) {
        ExtensionPointRegistry extensionPointRegistry = ThreadLocalUtil.get().getExtensionPointRegistry();

        
        Message processMessage = this.executeCurrentActivityAndLookupNextTransitionRecursively(startActivity, context);
        
        
//        ProcessInstance processInstance = context.getProcessInstance();
//        if (!processMessage.isSuspend()) {
//          //TODO 触发流程启动事件
////            this.fireEvent(PvmEventConstant.PROCESS_END.name(), context);
//            processInstance.setStatus(InstanceStatus.completed);
//        } else {
//            processInstance.setStatus(InstanceStatus.suspended);
//        }
        //TODO 这边怎么突然来了一个存储 存储流程实例
        ProcessInstanceStorage processInstanceStorage = extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
//        processInstanceStorage.save(context.getProcessInstance());
        return processMessage;
    }
    
    private Message executeCurrentActivityAndLookupNextTransitionRecursively(PvmActivity pvmActivity, ExecutionContext context) {
        // 执行当前节点,会触发当前节点的行为执行
        Message activityExecuteMessage = pvmActivity.execute(context);

        if(context.isNeedPause()){
            return activityExecuteMessage;
        }

        Message processMessage = new DefaultMessage();
        if (activityExecuteMessage.isSuspend()) {
            processMessage.setSuspend(true);
            return processMessage;
        }

        // 执行后续节点选择,实际上会调用此类 DefaultActivityTransitionSelectInvoker
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
                            // 执行Transition,目前仅是fireEvent,没做其他逻辑 
                            pvmTransition.execute(context);
                            PvmActivity targetPvmActivity = pvmTransition.getTarget();
                            //递归调用 执行Activity
                            this.executeCurrentActivityAndLookupNextTransitionRecursively(targetPvmActivity, context);
                        }else{
                            throw new EngineException("unsupported class type:ExecutionInstance");
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

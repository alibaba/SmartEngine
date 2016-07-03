package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.artifact.Process;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.service.ExecutionService;

/**
 * DefaultRuntimeProcess Created by ettear on 16-4-12.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultRuntimeProcess extends AbstractRuntimeActivity<Process> implements PvmProcess {

    private String                         uri;

    private ClassLoader                    classLoader;

    private Map<String, PvmActivity>   activities;

    private Map<String, PvmTransition> transitions;

    private PvmActivity                startActivity;

    @Override
    public Process getModel() {
        return (Process) super.getModel();
    }

    @Override
    public Message run(InstanceContext context) {
        // 从扩展注册机获取实例工厂
        ExecutionInstanceFactory executionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(ExecutionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(ActivityInstanceFactory.class);
//        InstanceFactFactory factFactory = this.getExtensionPointRegistry().getExtensionPoint(InstanceFactFactory.class);

        // 流程实例ID
        ProcessInstance processInstance = context.getProcessInstance();
        String processInstanceId = processInstance.getInstanceId();
        // 构建活动实例: 指向开始节点
        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setProcessInstanceId(processInstanceId);
        activityInstance.setActivityId(this.startActivity.getId());
        // 构建执行实例
        ExecutionInstance executionInstance = context.getCurrentExecution();
        if (null == executionInstance) {
            executionInstance = executionInstanceFactory.create();
            executionInstance.setProcessInstanceId(processInstanceId);
//            processInstance.setFact(factFactory.create());
            processInstance.addExecution(executionInstance);// 执行实例添加到流程实例
            context.setCurrentExecution(executionInstance);// 执行实例添加到当前上下文中
        }
        executionInstance.setActivity(activityInstance);

        // 状态
        processInstance.setStatus(InstanceStatus.running);

        // 执行流程启动事件
        this.invoke(AtomicOperationEvent.PROCESS_START.name(), context);
        // 从开始节点开始执行
        return this.runProcess(this.startActivity, context);
    }

    @Override
    public Message resume(InstanceContext context) {
        ProcessInstance processInstance = context.getProcessInstance();

        if (InstanceStatus.completed == processInstance.getStatus()) {
            return new DefaultMessage();
        }

        ExecutionInstance currentExecutionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = currentExecutionInstance.getActivity();

        String activityId = activityInstance.getActivityId();
        PvmActivity runtimeActivity = this.getActivities().get(activityId);

        // 状态
        processInstance.setStatus(InstanceStatus.running);

        Message processMessage = this.runProcess(runtimeActivity, context);
        if (!processMessage.isSuspend()) {
            ExecutionService executionManager = this.getExtensionPointRegistry().getExtensionPoint(ExecutionService.class);

            Map<String, Object> variables = new HashMap<>();
            // TODO ettear 或者用子流程事实做为主流程活动事实?
//            variables.putAll(context.getProcessInstance().getFact());
//            variables.putAll(context.getCurrentExecution().getFact());
            ProcessInstance parentProcessInstance = executionManager.signal(processInstance.getParentInstanceId(),
                                                                            processInstance.getParentExecutionInstanceId(),
                                                                            variables);
            if (null != parentProcessInstance && null != parentProcessInstance.getExecutions()) {
                for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : parentProcessInstance.getExecutions().entrySet()) {
                    if (executionInstanceEntry.getValue().isSuspend()) {
                        processMessage.setSuspend(true);
                        break;
                    }
                }
            }
        }
        return processMessage;
    }

    @Override
    protected Message doExecute(InstanceContext context) {
        ExecutionInstance currentExecutionInstance = context.getCurrentExecution();
        ActivityInstance activityInstance = currentExecutionInstance.getActivity();

        // 查询子流程
        ProcessInstanceStorage processInstanceStorage = this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceStorage.class);
        ProcessInstance subProcessInstance = processInstanceStorage.findSubProcess(activityInstance.getInstanceId());
        // 子流程没有结束
        if (InstanceStatus.completed != subProcessInstance.getStatus()) {
            activityInstance.setStatus(InstanceStatus.suspended);
            Message message = new DefaultMessage();
            message.setSuspend(true);
            return message;
        }

        // 重置状态
        currentExecutionInstance.setStatus(InstanceStatus.suspended);

        // 创建子流程上下文
        InstanceContextFactory instanceContextFactory = this.getExtensionPointRegistry().getExtensionPoint(InstanceContextFactory.class);
        ProcessInstanceFactory processInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceFactory.class);
//        InstanceFactFactory factFactory = this.getExtensionPointRegistry().getExtensionPoint(InstanceFactFactory.class);

        InstanceContext subInstanceContext = instanceContextFactory.create();
        ProcessInstance processInstance = processInstanceFactory.create();
        processInstance.setProcessUri(this.getUri());
        processInstance.setParentInstanceId(currentExecutionInstance.getProcessInstanceId());
        processInstance.setParentExecutionInstanceId(currentExecutionInstance.getInstanceId());
        processInstance.setParentActivityInstanceId(currentExecutionInstance.getActivity().getInstanceId());
//        processInstance.setFact(factFactory.create());
        subInstanceContext.setProcessInstance(processInstance);
        // TODO ettear 或者用主流程活动事实做为子流程事实?
        // subInstanceContext.setExecutionFact(context.getExecutionFact());
        // 运行子流程
        return this.run(subInstanceContext);
    }

    private Message runProcess(PvmActivity startActivity, InstanceContext context) {
        Message processMessage = this.executeActivity(startActivity, context);
        ProcessInstance processInstance = context.getProcessInstance();
        if (!processMessage.isSuspend()) {
            // 流程结束
            this.invoke(AtomicOperationEvent.PROCESS_END.name(), context);
            processInstance.setStatus(InstanceStatus.completed);
        } else {
            processInstance.setStatus(InstanceStatus.suspended);
        }
        // 存储流程实例
        this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceStorage.class).save(context.getProcessInstance());
        return processMessage;
    }

    private Message executeActivity(PvmActivity runtimeActivity, InstanceContext context) {
        // 执行当前节点
        Message activityExecuteMessage = runtimeActivity.execute(context);

        Message processMessage = new DefaultMessage();
        if (activityExecuteMessage.isSuspend()) {
            processMessage.setSuspend(true);
            return processMessage;
        }

        // 执行后续节点选择
        Message transitionSelectMessage = runtimeActivity.invoke(AtomicOperationEvent.ACTIVITY_TRANSITION_SELECT.name(),
                                                                 context);
        if (null != transitionSelectMessage) {
            Object transitionSelectBody = transitionSelectMessage.getBody();
            if (null != transitionSelectBody && transitionSelectBody instanceof List) {
                List<?> executionObjects = (List<?>) transitionSelectBody;
                if (!executionObjects.isEmpty()) {
                    // TODO ettear 多线程
                    for (Object executionObject : executionObjects) {
                        // 执行所有实例
                        if (executionObject instanceof ExecutionInstance) {
                            ExecutionInstance executionInstance = (ExecutionInstance) executionObject;
                            context.setCurrentExecution(executionInstance);

                            TransitionInstance transitionInstance = executionInstance.getActivity().getIncomeTransitions().get(0);
                            PvmTransition runtimeTransition = this.transitions.get(transitionInstance.getTransitionId());
                            // 执行Transition
                            runtimeTransition.execute(context);
                            PvmActivity target = runtimeTransition.getTarget();
                            // 执行Activity
                            this.executeActivity(target, context);
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

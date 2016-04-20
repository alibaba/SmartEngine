package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.manager.ExecutionManager;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.invocation.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DefaultRuntimeProcess
 * Created by ettear on 16-4-12.
 */
@Data
public class DefaultRuntimeProcess extends DefaultRuntimeActivity<Process> implements RuntimeProcess {

    private ClassLoader classLoader;

    private Map<String, RuntimeActivity> activities;

    private Map<String, RuntimeTransition> transitions;

    private RuntimeActivity startActivity;

    @Override
    public Message run(InstanceContext context) {
        //从扩展注册机获取实例工厂
        ExecutionInstanceFactory executionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ExecutionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ActivityInstanceFactory.class);

        //流程实例ID
        ProcessInstance processInstance = context.getProcessInstance();
        String processInstanceId = processInstance.getInstanceId();
        //构建活动实例: 指向开始节点
        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setInstanceId(InstanceIdUtils.uuid());
        activityInstance.setProcessInstanceId(processInstanceId);
        activityInstance.setActivityId(this.startActivity.getId());
        //构建执行实例
        ExecutionInstance executionInstance = executionInstanceFactory.create();
        executionInstance.setInstanceId(InstanceIdUtils.uuid());
        executionInstance.setProcessInstanceId(processInstanceId);
        executionInstance.setActivity(activityInstance);
        context.setCurrentExecution(executionInstance);//执行实例添加到当前上下文中
        processInstance.addExecution(executionInstance);//执行实例添加到流程实例
        //执行流程启动事件
        this.invoke(AtomicOperationEvent.PROCESS_START.name(),
                    context);
        //从开始节点开始执行
        return this.runProcess(this.startActivity, context);
    }

    @Override
    public Message resume(InstanceContext context) {
        ProcessInstance processInstance=context.getProcessInstance();
        ExecutionInstance currentExecutionInstance=context.getCurrentExecution();
        ActivityInstance activityInstance = currentExecutionInstance.getActivity();
        String activityId = activityInstance.getActivityId();
        RuntimeActivity runtimeActivity=this.getActivities().get(activityId);

        Message processMessage =  this.runProcess(runtimeActivity, context);
        if(!processMessage.isSuspend()){
            ExecutionManager executionManager=this.getExtensionPointRegistry().getExtensionPoint(ExecutionManager.class);

            Map<String,Object> variables=new HashMap<>();
            //TODO 执行结果放入
            ProcessInstance parentProcessInstance=executionManager.signal(
                    processInstance.getParentInstanceId(), processInstance.getParentExecutionInstanceId(),
                                                         variables);
            if(null!=parentProcessInstance && null!=parentProcessInstance.getExecutions()){
                for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : parentProcessInstance.getExecutions().entrySet()) {
                    if(executionInstanceEntry.getValue().isSuspend()){
                        processMessage.setSuspend(true);
                        break;
                    }
                }
            }
        }
        return processMessage;
    }

    @Override
    public Message execute(InstanceContext context) {
        //TODO ettear resume
        ExecutionInstance currentExecutionInstance=context.getCurrentExecution();
        currentExecutionInstance.setSuspend(true);

        //创建子流程上下文
        InstanceContextFactory instanceContextFactory = this.getExtensionPointRegistry().getExtensionPoint(
                InstanceContextFactory.class);
        ProcessInstanceFactory processInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ProcessInstanceFactory.class);

        InstanceContext subInstanceContext = instanceContextFactory.create();
        ProcessInstance processInstance=processInstanceFactory.create();
        processInstance.setProcessId(this.getId());
        //processInstance.getProcessVersion(this.get); TODO ettear add version;
        processInstance.setParentInstanceId(currentExecutionInstance.getProcessInstanceId());
        processInstance.setParentExecutionInstanceId(currentExecutionInstance.getInstanceId());
        subInstanceContext.setProcessInstance(processInstance);
        //运行子流程
        Message processMessage=this.run(subInstanceContext);
        if(!processMessage.isSuspend()){
            //如果子流程结束，当前流程继续执行
            currentExecutionInstance.setSuspend(false);
        }
        return processMessage;
    }

    private Message runProcess(RuntimeActivity startActivity, InstanceContext context){
        Message processMessage = this.executeActivity(startActivity, context);
        //存储
        this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceStorage.class).save(context.getProcessInstance());
        if(!processMessage.isSuspend()) {
            this.invoke(AtomicOperationEvent.PROCESS_END.name(),
                        context);
        }
        return processMessage;
    }

    private Message executeActivity(RuntimeActivity runtimeActivity, InstanceContext context) {
        //执行当前节点
        Message activityExecuteMessage = runtimeActivity.execute(context);

        Message processMessage=new DefaultMessage();
        if (activityExecuteMessage.isSuspend()) {
            processMessage.setSuspend(true);
            return processMessage;
        }

        //执行后续节点选择
        Message transitionSelectMessage = runtimeActivity.invoke(AtomicOperationEvent.ACTIVITY_TRANSITION_SELECT.name(),
                                                                 context);
        if (null != transitionSelectMessage) {
            Object transitionSelectBody = transitionSelectMessage.getBody();
            if (null != transitionSelectBody && transitionSelectBody instanceof List) {
                List<?> executionObjects = (List<?>) transitionSelectBody;
                if (!executionObjects.isEmpty()) {
                    for (Object executionObject : executionObjects) {
                        //执行所有实例
                        if (executionObject instanceof ExecutionInstance) {
                            ExecutionInstance executionInstance = (ExecutionInstance) executionObject;
                            context.setCurrentExecution(executionInstance);
                            TransitionInstance transitionInstance = executionInstance.getActivity().getIncomeTransitions().get(
                                    0);
                            RuntimeTransition runtimeTransition = this.transitions.get(
                                    transitionInstance.getTransitionId());
                            //执行Transition
                            runtimeTransition.execute(context);
                            RuntimeActivity target = runtimeTransition.getTarget();
                            //执行Activity
                            this.executeActivity(target, context);
                        }
                    }
                    Map<String, ExecutionInstance> executionInstances = context.getProcessInstance().getExecutions();
                    if (null != executionInstances && executionInstances.isEmpty()) {
                        for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : executionInstances.entrySet()) {
                            ExecutionInstance executionInstance = executionInstanceEntry.getValue();
                            if (executionInstance.isSuspend()) {//存在暂停的执行实例
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

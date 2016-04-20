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
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

    private Map<String, RuntimeSequenceFlow> sequenceFlows;

    private RuntimeActivity startActivity;

    @Override
    public boolean run(InstanceContext context) {
        //从扩展注册机获取实例工厂
        ProcessInstanceFactory processInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ProcessInstanceFactory.class);
        ExecutionInstanceFactory executionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ExecutionInstanceFactory.class);
        ActivityInstanceFactory activityInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ActivityInstanceFactory.class);

        //流程实例ID
        String processInstanceId = InstanceIdUtils.uuid();
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
        //构建流程实例
        ProcessInstance processInstance = processInstanceFactory.create();
        processInstance.setInstanceId(processInstanceId);
        processInstance.addExecution(executionInstance);
        //实例添加到当前上下文中
        context.setCurrentExecution(executionInstance);
        context.setProcessInstance(processInstance);
        //执行流程启动事件
        this.invoke(AtomicOperationEvent.PROCESS_START.name(),
                    context);
        //从开始节点开始执行
        return this.runProcess(this.startActivity, context);
    }

    @Override
    public boolean resume(InstanceContext context) {
        ActivityInstance activityInstance = context.getCurrentExecution().getActivity();
        String activityId = activityInstance.getActivityId();
        RuntimeActivity runtimeActivity=this.getActivities().get(activityId);

        boolean suspend =  this.runProcess(runtimeActivity, context);
        if(!suspend){
            ExecutionInstance parent=context.getParentExecution();
            //TODO ettear 调用parent执行 parent流程
        }
        return suspend;
    }

    @Override
    public boolean execute(InstanceContext context) {

        ExecutionInstance currentExecutionInstance=context.getCurrentExecution();
        currentExecutionInstance.setSuspend(true);

        //创建子流程上下文
        InstanceContextFactory instanceContextFactory = this.getExtensionPointRegistry().getExtensionPoint(
                InstanceContextFactory.class);
        InstanceContext subInstanceContext = instanceContextFactory.create();
        subInstanceContext.setParentExecution(currentExecutionInstance);
        //运行子流程
        boolean suspend=this.run(subInstanceContext);
        if(!suspend){
            currentExecutionInstance.setSuspend(false);
        }
        return suspend;
    }

    private boolean runProcess(RuntimeActivity startActivity, InstanceContext context){
        boolean suspend = this.executeActivity(startActivity, context);
        //存储
        this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceStorage.class).save(context.getProcessInstance());
        if(!suspend) {
            this.invoke(AtomicOperationEvent.PROCESS_END.name(),
                        context);
        }
        return suspend;
    }

    private boolean executeActivity(RuntimeActivity runtimeActivity, InstanceContext context) {
        boolean suspend = runtimeActivity.execute(context);
        if (suspend) {
            return true;
        }

        Message transitionSelectMessage = runtimeActivity.invoke(AtomicOperationEvent.ACTIVITY_TRANSITION_SELECT.name(),
                                                                 context);
        if (null != transitionSelectMessage) {
            Object transitionSelectBody = transitionSelectMessage.getBody();
            if (null != transitionSelectBody && transitionSelectBody instanceof List) {
                List<?> executionObjects = (List<?>) transitionSelectBody;
                if (!executionObjects.isEmpty()) {
                    for (Object executionObject : executionObjects) {
                        if (executionObject instanceof ExecutionInstance) {
                            ExecutionInstance executionInstance = (ExecutionInstance) executionObject;
                            context.setCurrentExecution(executionInstance);
                            TransitionInstance transitionInstance = executionInstance.getActivity().getIncomeTransitions().get(
                                    0);
                            RuntimeSequenceFlow runtimeSequenceFlow = this.sequenceFlows.get(
                                    transitionInstance.getSequenceFlowId());

                            runtimeSequenceFlow.execute(context);
                            RuntimeActivity target = runtimeSequenceFlow.getTarget();
                            this.executeActivity(target, context);
                        }
                    }
                    Map<String, ExecutionInstance> executionInstances = context.getProcessInstance().getExecutions();
                    if (null != executionInstances && executionInstances.isEmpty()) {
                        for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : executionInstances.entrySet()) {
                            ExecutionInstance executionInstance = executionInstanceEntry.getValue();
                            if (!StringUtils.equals("completed", executionInstance.getStatus())) {//TODO ettear
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}

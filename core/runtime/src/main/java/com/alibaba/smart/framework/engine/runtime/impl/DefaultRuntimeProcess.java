package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
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
    public boolean execute(InstanceContext context) {
        String processInstanceId = InstanceIdUtils.uuid();

        ProcessInstanceFactory processInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ProcessInstanceFactory.class);

        ExecutionInstanceFactory executionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ExecutionInstanceFactory.class);

        ActivityInstanceFactory activityInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(
                ActivityInstanceFactory.class);

        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setInstanceId(InstanceIdUtils.uuid());
        activityInstance.setProcessInstanceId(processInstanceId);
        activityInstance.setActivityId(this.startActivity.getId());

        ExecutionInstance executionInstance = executionInstanceFactory.create();
        executionInstance.setInstanceId(InstanceIdUtils.uuid());
        executionInstance.setProcessInstanceId(processInstanceId);
        executionInstance.setActivity(activityInstance);

        ProcessInstance processInstance = processInstanceFactory.create();
        processInstance.setInstanceId(processInstanceId);
        processInstance.addExecution(executionInstance);

        context.setCurrentExecution(executionInstance);
        context.setProcessInstance(processInstance);
        boolean suspend = this.executeActivity(this.startActivity, context);

        this.getExtensionPointRegistry().getExtensionPoint(ProcessInstanceStorage.class).save(processInstance);

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

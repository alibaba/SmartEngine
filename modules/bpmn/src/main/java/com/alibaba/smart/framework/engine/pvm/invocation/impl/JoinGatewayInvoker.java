package com.alibaba.smart.framework.engine.pvm.invocation.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;
import com.alibaba.smart.framework.engine.model.instance.*;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ettear on 16-5-4.
 */
public class JoinGatewayInvoker implements Invoker {

    private ExtensionPointRegistry extensionPointRegistry;
    private PvmActivity        runtimeActivity;

    public JoinGatewayInvoker(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.runtimeActivity = runtimeActivity;
    }

    // TODO ettear 考虑并发Join问题
    @Override
    public Message invoke(ExecutionContext context) {

        if (this.runtimeActivity.getIncomeTransitions().size() == 1) {
            return new DefaultMessage();
        }

        ProcessInstance processInstance = context.getProcessInstance();
        Map<String, ExecutionInstance> executionInstances = processInstance.getExecutions();

        // Found execution instance on this activity
        Map<String, ExecutionInstance> joinExecutionInstance = new HashMap<>();
        for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : executionInstances.entrySet()) {
            ExecutionInstance executionInstance = executionInstanceEntry.getValue();
            String activityId = executionInstance.getActivity().getActivityId();
            if (StringUtils.equals(this.runtimeActivity.getModel().getId(), activityId)) {
                joinExecutionInstance.put(executionInstance.getInstanceId(), executionInstance);
            }
        }


        boolean completed = true;
        if (executionInstances.values().stream().anyMatch(p->!p.getActivity().getActivityId().equals(runtimeActivity.getModel().getId()))) {
            completed = false;
        }

        ActivityInstanceFactory activityInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);

        ActivityInstance activityInstance = activityInstanceFactory.create();
        activityInstance.setActivityId(this.runtimeActivity.getModel().getId());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        if (completed) {
            ExecutionInstanceFactory executionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
//            InstanceFactFactory factFactory = this.extensionPointRegistry.getExtensionPoint(InstanceFactFactory.class);

            ExecutionInstance newExecutionInstance = executionInstanceFactory.create();
            newExecutionInstance.setInstanceId(InstanceIdUtil.uuid());
            newExecutionInstance.setProcessInstanceId(processInstance.getInstanceId());
//            newExecutionInstance.setFact(factFactory.create());
            newExecutionInstance.setActivity(activityInstance);

            processInstance.addExecution(newExecutionInstance);
            context.setCurrentExecution(newExecutionInstance);

            for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : joinExecutionInstance.entrySet()) {
                ExecutionInstance executionInstance = executionInstanceEntry.getValue();
                executionInstance.setCompleteDate(new Date());
                executionInstance.setStatus(InstanceStatus.completed);
                processInstance.removeExecution(executionInstance.getInstanceId());
            }
            return new DefaultMessage();
        } else {
            Message message = new DefaultMessage();
            message.setSuspend(true);
            return message;
        }
    }
}

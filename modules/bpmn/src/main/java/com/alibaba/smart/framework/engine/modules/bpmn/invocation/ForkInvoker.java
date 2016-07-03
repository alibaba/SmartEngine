package com.alibaba.smart.framework.engine.modules.bpmn.invocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.invocation.impl.AbstractTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by ettear on 16-5-4.
 */
public class ForkInvoker extends AbstractTransitionSelectInvoker {

    public ForkInvoker(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
                                                       ProcessInstance processInstance,
                                                       ExecutionInstance currentExecutionInstance,
                                                       ActivityInstance currentActivityInstance) {
        ExecutionInstanceFactory executionInstanceFactory = this.getExtensionPointRegistry().getExtensionPoint(ExecutionInstanceFactory.class);
//        InstanceFactFactory factFactory = this.getExtensionPointRegistry().getExtensionPoint(InstanceFactFactory.class);

        currentExecutionInstance.setStatus(InstanceStatus.completed);
        currentExecutionInstance.setCompleteDate(new Date());
        processInstance.removeExecution(currentExecutionInstance.getInstanceId());

        List<ExecutionInstance> executions = new ArrayList<>();
        for (PvmTransition transition : transitions) {
            ExecutionInstance executionInstance = executionInstanceFactory.create();
            executionInstance.setInstanceId(InstanceIdUtil.uuid());
            executionInstance.setProcessInstanceId(processInstance.getInstanceId());
//            executionInstance.setFact(factFactory.create());
            this.buildExecutionInstance(transition, processInstance, executionInstance, currentActivityInstance);
            processInstance.addExecution(executionInstance);
            executions.add(executionInstance);
        }

        return executions;
    }
}

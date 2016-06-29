package com.alibaba.smart.framework.engine.modules.bpmn.invocation;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.invocation.impl.AbstractTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * ExclusiveInvoker Created by ettear on 16-5-4.
 */
public class ExclusiveInvoker extends AbstractTransitionSelectInvoker {

    public ExclusiveInvoker(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
                                                       ProcessInstance processInstance,
                                                       ExecutionInstance currentExecutionInstance,
                                                       ActivityInstance currentActivityInstance) {
        PvmTransition runtimeTransition = transitions.get(0);
        this.buildExecutionInstance(runtimeTransition, processInstance, currentExecutionInstance,
                                    currentActivityInstance);
        List<ExecutionInstance> executions = new ArrayList<>();
        executions.add(currentExecutionInstance);
        return executions;
    }
}

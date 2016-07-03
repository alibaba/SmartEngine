package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * DefaultActivityTransitionSelectInvoker Created by ettear on 16-4-19.
 */
public class DefaultActivityTransitionSelectInvoker extends AbstractTransitionSelectInvoker {

    public DefaultActivityTransitionSelectInvoker(ExtensionPointRegistry extensionPointRegistry,
                                                  PvmActivity runtimeActivity) {
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

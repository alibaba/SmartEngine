package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * DefaultActivityTransitionSelectInvoker
 * Created by ettear on 16-4-19.
 */
public class DefaultActivityTransitionSelectInvoker extends AbstractTransitionSelectInvoker {

    public DefaultActivityTransitionSelectInvoker(ExtensionPointRegistry extensionPointRegistry,RuntimeActivity runtimeActivity) {
        super(extensionPointRegistry,runtimeActivity);
    }

    @Override
    protected List<ExecutionInstance> processExecution(List<RuntimeTransition> transitions,
                                                       ProcessInstance processInstance,
                                                       ExecutionInstance currentExecutionInstance,
                                                       ActivityInstance currentActivityInstance) {
        RuntimeTransition runtimeTransition = transitions.get(0);
        this.buildExecutionInstance(runtimeTransition, processInstance, currentExecutionInstance,
                                    currentActivityInstance);
        List<ExecutionInstance> executions = new ArrayList<>();
        executions.add(currentExecutionInstance);
        return executions;
    }
}

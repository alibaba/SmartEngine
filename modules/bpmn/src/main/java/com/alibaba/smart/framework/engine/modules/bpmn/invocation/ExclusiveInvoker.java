package com.alibaba.smart.framework.engine.modules.bpmn.invocation;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.invocation.impl.AbstractTransitionSelectInvoker;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

import java.util.ArrayList;
import java.util.List;

/**
 * ExclusiveInvoker
 * Created by ettear on 16-5-4.
 */
public class ExclusiveInvoker extends AbstractTransitionSelectInvoker {

    public ExclusiveInvoker(
            ExtensionPointRegistry extensionPointRegistry,RuntimeActivity runtimeActivity) {
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

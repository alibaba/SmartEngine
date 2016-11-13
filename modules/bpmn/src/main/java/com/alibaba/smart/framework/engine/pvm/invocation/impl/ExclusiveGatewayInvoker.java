package com.alibaba.smart.framework.engine.pvm.invocation.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.impl.AbstractGatewayInvoker;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * ExclusiveGatewayInvoker Created by ettear on 16-5-4.
 */
public class ExclusiveGatewayInvoker extends AbstractGatewayInvoker {

    public ExclusiveGatewayInvoker(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super( runtimeActivity);
    }

//    @Override
//    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
//                                                       ProcessInstance processInstance,
//                                                       ExecutionInstance currentExecutionInstance,
//                                                       ActivityInstance currentActivityInstance) {
//        PvmTransition runtimeTransition = transitions.get(0);
////        this.buildExecutionInstance(runtimeTransition, processInstance, currentExecutionInstance,
////                                    currentActivityInstance);
//        List<ExecutionInstance> executions = new ArrayList<>();
//        executions.add(currentExecutionInstance);
//        return executions;
//    }

    @Override
    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions, ProcessInstance processInstance, ExecutionInstance currentExecutionInstance) {
        return null;
    }
}

package com.alibaba.smart.framework.engine.invocation.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultActivityTransitionSelectInvoker extends AbstractGatewayInvoker {

    public DefaultActivityTransitionSelectInvoker(PvmActivity runtimeActivity) {
        super( runtimeActivity);
    }

    @Override
    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions, ProcessInstance processInstance, ExecutionInstance currentExecutionInstance) {
        return null;
    }

//    @Override
//    protected List<ExecutionInstance> processExecution(List<PvmTransition> transitions,
//                                                       ProcessInstance processInstance
//                                                       ) {
//        PvmTransition runtimeTransition = transitions.get(0);
//        this.buildExecutionInstance(runtimeTransition, processInstance,null);
//        List<ExecutionInstance> executions = new ArrayList<>();
//        return executions;
//    }
}

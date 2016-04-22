package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.SequenceFlow;


public class SequenceFlowProvider extends AbstractTransitionProvider<SequenceFlow> implements TransitionProvider<SequenceFlow> {

    public SequenceFlowProvider(RuntimeTransition runtimeTransition) {
       super(runtimeTransition);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new DefaultInvoker("Execute sequence flow " + this.getRuntimeTransition().getId());
    }
}
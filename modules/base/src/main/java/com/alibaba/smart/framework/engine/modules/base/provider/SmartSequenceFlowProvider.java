package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.modules.base.invocation.SmartInvoker;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractTransitionProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProvider extends AbstractTransitionProvider<SmartSequenceFlow> implements TransitionProvider<SmartSequenceFlow> {

    public SmartSequenceFlowProvider(RuntimeTransition runtimeTransition) {
       super(runtimeTransition);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new SmartInvoker("Execute sequence flow " + this.getRuntimeTransition().getId());
    }
}

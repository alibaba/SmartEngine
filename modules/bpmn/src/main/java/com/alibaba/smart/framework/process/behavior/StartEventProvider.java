package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.StartEvent;


public class StartEventProvider extends AbstractActivityProvider<StartEvent>
        implements ActivityProvider<StartEvent> {

    public StartEventProvider(RuntimeActivity runtimeActivity) {
        super(runtimeActivity);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new DefaultInvoker("Execute activity " + this.getRuntimeActivity().getId());
    }
}

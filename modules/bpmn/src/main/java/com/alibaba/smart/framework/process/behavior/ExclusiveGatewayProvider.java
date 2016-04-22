package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ExclusiveGateway;

public class ExclusiveGatewayProvider extends AbstractActivityProvider<ExclusiveGateway> implements ActivityProvider<ExclusiveGateway> {

    public ExclusiveGatewayProvider(RuntimeActivity runtimeActivity) {
        super(runtimeActivity);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new DefaultInvoker("Execute activity " + this.getRuntimeActivity().getId());
    }
}

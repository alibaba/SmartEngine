package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.process.behavior.DefaultInvoker;

public class ExclusiveGatewayProvider extends AbstractActivityProvider<ExclusiveGateway> implements ActivityProvider<ExclusiveGateway> {

    public ExclusiveGatewayProvider(RuntimeActivity runtimeActivity) {
        super(runtimeActivity);
    }
}

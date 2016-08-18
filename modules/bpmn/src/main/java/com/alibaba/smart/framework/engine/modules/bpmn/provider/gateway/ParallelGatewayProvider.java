package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.invocation.impl.ForkGatewayInvoker;
import com.alibaba.smart.framework.engine.pvm.invocation.impl.JoinGatewayInvoker;

public class ParallelGatewayProvider extends AbstractBpmnActivityProvider<ParallelGateway> implements ActivityProvider<ParallelGateway> {

    public ParallelGatewayProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected Invoker createStartInvoker() {
        return new JoinGatewayInvoker(this.getExtensionPointRegistry(), this.getRuntimeActivity());
    }

    @Override
    protected Invoker createTransitionSelectInvoker() {
        return new ForkGatewayInvoker(this.getExtensionPointRegistry(), this.getRuntimeActivity());
    }
}

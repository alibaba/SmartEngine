package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.invocation.ForkInvoker;
import com.alibaba.smart.framework.engine.modules.bpmn.invocation.JoinInvoker;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

public class ParallelGatewayProvider extends AbstractBpmnActivityProvider<ParallelGateway> implements ActivityProvider<ParallelGateway> {

    public ParallelGatewayProvider(ExtensionPointRegistry extensionPointRegistry,RuntimeActivity runtimeActivity) {
        super(extensionPointRegistry,runtimeActivity);
    }

    @Override
    protected Invoker createStartInvoker() {
        return new JoinInvoker(this.getExtensionPointRegistry(),this.getRuntimeActivity());
    }

    @Override
    protected Invoker createTransitionSelectInvoker() {
        return new ForkInvoker(this.getExtensionPointRegistry(),this.getRuntimeActivity());
    }
}

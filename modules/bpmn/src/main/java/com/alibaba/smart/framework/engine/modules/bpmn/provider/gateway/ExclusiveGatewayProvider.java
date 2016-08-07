package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.invocation.ExclusiveInvoker;

public class ExclusiveGatewayProvider extends AbstractBpmnActivityProvider<ExclusiveGateway> implements ActivityProvider<ExclusiveGateway> {

    public ExclusiveGatewayProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected Invoker createTransitionSelectInvoker() {
        return new ExclusiveInvoker(this.getExtensionPointRegistry(), this.getRuntimeActivity());
    }
}

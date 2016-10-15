package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.invocation.impl.ExclusiveGatewayInvoker;

public class ExclusiveGatewayBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<ExclusiveGateway> implements ActivityBehaviorProvider<ExclusiveGateway> {

    public ExclusiveGatewayBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    protected Invoker createTransitionSelectInvoker() {
        return new ExclusiveGatewayInvoker(this.getExtensionPointRegistry(), this.getRuntimeActivity());
    }
}

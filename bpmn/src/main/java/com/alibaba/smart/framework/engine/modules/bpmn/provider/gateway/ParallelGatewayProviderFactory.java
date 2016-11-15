package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ParallelGatewayProviderFactory implements ActivityProviderFactory<ParallelGateway> {

    private ExtensionPointRegistry extensionPointRegistry;

    public ParallelGatewayProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ParallelGatewayBehaviorProvider createActivityProvider(PvmActivity activity) {
        return new ParallelGatewayBehaviorProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }
}

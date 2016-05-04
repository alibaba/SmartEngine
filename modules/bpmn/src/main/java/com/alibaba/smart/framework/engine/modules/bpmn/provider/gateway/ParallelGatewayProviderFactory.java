package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

public class ParallelGatewayProviderFactory implements ActivityProviderFactory<ParallelGateway> {

    private ExtensionPointRegistry extensionPointRegistry;

    public ParallelGatewayProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ParallelGatewayProvider createActivityProvider(RuntimeActivity activity) {
        return new ParallelGatewayProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }
}

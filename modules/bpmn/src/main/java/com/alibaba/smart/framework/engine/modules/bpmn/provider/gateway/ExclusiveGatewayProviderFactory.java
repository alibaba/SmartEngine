package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

public class ExclusiveGatewayProviderFactory implements ActivityProviderFactory<ExclusiveGateway> {

    private ExtensionPointRegistry extensionPointRegistry;

    public ExclusiveGatewayProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ExclusiveGatewayProvider createActivityProvider(RuntimeActivity activity) {
        return new ExclusiveGatewayProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        return ExclusiveGateway.class;
    }
}

package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ExclusiveGateway;


public class ExclusiveGatewayProviderFactory implements ActivityProviderFactory<ExclusiveGateway> {

    @Override
    public ExclusiveGatewayProvider createActivityProvider(RuntimeActivity activity) {
        return new ExclusiveGatewayProvider(activity);
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        return ExclusiveGateway.class;
    }
}

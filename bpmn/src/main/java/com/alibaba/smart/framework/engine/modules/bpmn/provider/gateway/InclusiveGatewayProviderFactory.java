package com.alibaba.smart.framework.engine.modules.bpmn.provider.gateway;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by niefeng on 2018/4/18.
 */
public class InclusiveGatewayProviderFactory implements ActivityProviderFactory<InclusiveGateway> {

    private ExtensionPointRegistry extensionPointRegistry;

    public InclusiveGatewayProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ActivityBehavior createActivityProvider(PvmActivity activity) {
        return new ExclusiveGatewayBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<InclusiveGateway> getModelType() {
        return InclusiveGateway.class;
    }
}

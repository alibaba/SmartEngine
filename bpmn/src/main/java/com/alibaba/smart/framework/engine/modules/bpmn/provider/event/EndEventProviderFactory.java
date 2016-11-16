package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class EndEventProviderFactory implements ActivityProviderFactory<EndEvent> {

    private ExtensionPointRegistry extensionPointRegistry;

    public EndEventProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public EndEventBehavior createActivityProvider(PvmActivity activity) {
        //TODO  减少每次都 new,其他类也类似
        return new EndEventBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }
}

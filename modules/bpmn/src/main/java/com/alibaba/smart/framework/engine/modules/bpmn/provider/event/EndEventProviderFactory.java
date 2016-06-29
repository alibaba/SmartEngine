package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class EndEventProviderFactory implements ActivityProviderFactory<EndEvent> {

    private ExtensionPointRegistry extensionPointRegistry;

    public EndEventProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public EndEventProvider createActivityProvider(PvmActivity activity) {
        return new EndEventProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class StartEventProviderFactory implements ActivityProviderFactory<StartEvent> {

    private ExtensionPointRegistry extensionPointRegistry;

    public StartEventProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public StartEventBehaviorProvider createActivityProvider(PvmActivity activity) {
        return new StartEventBehaviorProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }
}

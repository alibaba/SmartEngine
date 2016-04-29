package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;


public class StartEventProviderFactory implements ActivityProviderFactory<StartEvent> {

    private ExtensionPointRegistry extensionPointRegistry;
    public StartEventProviderFactory(ExtensionPointRegistry extensionPointRegistry){
        this.extensionPointRegistry=extensionPointRegistry;
    }
    @Override
    public StartEventProvider createActivityProvider(RuntimeActivity activity) {
        return new StartEventProvider(this.extensionPointRegistry,activity);
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }
}

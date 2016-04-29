package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;


public class EndEventProviderFactory implements ActivityProviderFactory<EndEvent> {
    private ExtensionPointRegistry extensionPointRegistry;
    public EndEventProviderFactory(ExtensionPointRegistry extensionPointRegistry){
        this.extensionPointRegistry=extensionPointRegistry;
    }

    @Override
    public EndEventProvider createActivityProvider(RuntimeActivity activity) {
        return new EndEventProvider(this.extensionPointRegistry,activity);
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }
}

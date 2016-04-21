package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.StartEvent;


public class StartEventProviderFactory implements ActivityProviderFactory<StartEvent> {

    @Override
    public StartEventProvider createActivityProvider(RuntimeActivity activity) {
        return new StartEventProvider(activity);
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }
}

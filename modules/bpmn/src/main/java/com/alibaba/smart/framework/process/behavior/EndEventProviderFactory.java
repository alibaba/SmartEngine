package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.EndEvent;


public class EndEventProviderFactory implements ActivityProviderFactory<EndEvent> {

    @Override
    public EndEventProvider createActivityProvider(RuntimeActivity activity) {
        return new EndEventProvider(activity);
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }
}

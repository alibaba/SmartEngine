package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.Process;


public class ProcessProviderFactory implements ActivityProviderFactory<Process> {

    @Override
    public ProcessProvider createActivityProvider(RuntimeActivity activity) {
        return new ProcessProvider(activity);
    }

    @Override
    public Class<Process> getModelType() {
        return Process.class;
    }
}

package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.task.ServiceTask;


public class ServiceTaskProviderFactory implements ActivityProviderFactory<ServiceTask> {

    @Override
    public ServiceTaskProvider createActivityProvider(RuntimeActivity activity) {
        return new ServiceTaskProvider(activity);
    }

    @Override
    public Class<ServiceTask> getModelType() {
        return ServiceTask.class;
    }
}

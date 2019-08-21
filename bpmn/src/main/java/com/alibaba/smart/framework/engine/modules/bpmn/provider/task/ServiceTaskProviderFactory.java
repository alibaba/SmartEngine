package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class ServiceTaskProviderFactory implements ActivityProviderFactory<ServiceTask> {

    private ExtensionPointRegistry extensionPointRegistry;

    public ServiceTaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public ServiceTaskBehavior createActivityProvider(PvmActivity activity) {
        return new ServiceTaskBehavior(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<ServiceTask> getModelType() {
        return ServiceTask.class;
    }
}

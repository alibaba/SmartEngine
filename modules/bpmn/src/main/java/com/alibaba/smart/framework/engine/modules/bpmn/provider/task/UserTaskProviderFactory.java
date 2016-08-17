package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.provider.factory.ActivityProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class UserTaskProviderFactory implements ActivityProviderFactory<UserTask> {

    private ExtensionPointRegistry extensionPointRegistry;

    public UserTaskProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public UserTaskProvider createActivityProvider(PvmActivity activity) {
        return new UserTaskProvider(this.extensionPointRegistry, activity);
    }

    @Override
    public Class<UserTask> getModelType() {
        return UserTask.class;
    }
}

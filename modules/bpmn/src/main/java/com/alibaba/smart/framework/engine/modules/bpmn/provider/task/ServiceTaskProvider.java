package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

public class ServiceTaskProvider extends AbstractBpmnActivityProvider<ServiceTask> implements ActivityProvider<ServiceTask> {

    public ServiceTaskProvider(ExtensionPointRegistry extensionPointRegistry, RuntimeActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.task;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityBehavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(type = ExtensionConstant.ACTIVITY_BEHAVIOR, bindingTo = ServiceTask.class)

public class ServiceTaskBehavior extends AbstractActivityBehavior<ServiceTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskBehavior.class);


    //public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}

    public ServiceTaskBehavior() {
        super();
    }




}

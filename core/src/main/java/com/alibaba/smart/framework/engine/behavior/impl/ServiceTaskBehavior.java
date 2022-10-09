package com.alibaba.smart.framework.engine.behavior.impl;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ServiceTask.class)
public class ServiceTaskBehavior extends AbstractActivityBehavior<ServiceTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskBehavior.class);


    //public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}

    public ServiceTaskBehavior() {
        super();
    }




}
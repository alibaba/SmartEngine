package com.alibaba.smart.framework.engine.extendsion.parser.engine.behavior.impl;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.BusinessRuleTask;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = BusinessRuleTask.class)

public class BusinessRuleTaskBehavior extends AbstractActivityBehavior<BusinessRuleTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRuleTaskBehavior.class);

    //public ServiceTaskBehavior(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
    //    super(extensionPointRegistry, runtimeActivity);
    //}

    public BusinessRuleTaskBehavior() {
        super();
    }



}

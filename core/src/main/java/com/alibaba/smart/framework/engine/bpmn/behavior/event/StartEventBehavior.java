package com.alibaba.smart.framework.engine.bpmn.behavior.event;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = StartEvent.class)

public class StartEventBehavior extends AbstractActivityBehavior<StartEvent> {


    public StartEventBehavior() {
        super();
    }
}

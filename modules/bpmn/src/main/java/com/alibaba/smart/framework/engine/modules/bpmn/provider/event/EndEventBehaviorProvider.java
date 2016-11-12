package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

public class EndEventBehaviorProvider extends AbstractBpmnActivityBehaviorProvider<EndEvent> implements ActivityBehaviorProvider<EndEvent> {

    public EndEventBehaviorProvider(ExtensionPointRegistry extensionPointRegistry, PvmActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {
        context.setNeedPause(true);
    }
}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.process.behavior.DefaultInvoker;

public class StartEventProvider extends AbstractBpmnActivityProvider<StartEvent>
        implements ActivityProvider<StartEvent> {

    public StartEventProvider(
            ExtensionPointRegistry extensionPointRegistry,
            RuntimeActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }
}

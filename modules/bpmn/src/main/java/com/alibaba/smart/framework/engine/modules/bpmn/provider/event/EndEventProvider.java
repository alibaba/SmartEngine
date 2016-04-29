package com.alibaba.smart.framework.engine.modules.bpmn.provider.event;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.provider.process.AbstractBpmnActivityProvider;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.behavior.DefaultInvoker;

public class EndEventProvider extends AbstractBpmnActivityProvider<EndEvent>
        implements ActivityProvider<EndEvent> {

    public EndEventProvider(ExtensionPointRegistry extensionPointRegistry,
                            RuntimeActivity runtimeActivity) {
        super(extensionPointRegistry, runtimeActivity);
    }
}

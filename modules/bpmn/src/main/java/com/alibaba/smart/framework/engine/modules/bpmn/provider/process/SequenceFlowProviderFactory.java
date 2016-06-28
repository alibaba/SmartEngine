package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

public class SequenceFlowProviderFactory implements TransitionProviderFactory<SequenceFlow> {

    private ExtensionPointRegistry extensionPointRegistry;

    public SequenceFlowProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public SequenceFlowProvider createTransitionProvider(RuntimeTransition runtimeTransition) {
        return new SequenceFlowProvider(this.extensionPointRegistry, runtimeTransition);
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }

}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowProviderFactory implements TransitionProviderFactory<SequenceFlow> {

    private ExtensionPointRegistry extensionPointRegistry;

    public SequenceFlowProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public SequenceFlowProvider createTransitionProvider(PvmTransition runtimeTransition) {
        return new SequenceFlowProvider(this.extensionPointRegistry, runtimeTransition);
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }

}

package com.alibaba.smart.framework.engine.modules.bpmn.provider.process;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

public class SequenceFlowProviderFactory implements TransitionProviderFactory<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> {

    private ExtensionPointRegistry extensionPointRegistry;

    public SequenceFlowProviderFactory(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public SequenceFlow createTransitionProvider(PvmTransition runtimeTransition) {
        return new SequenceFlow(this.extensionPointRegistry, runtimeTransition);
    }

    @Override
    public Class<com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow> getModelType() {
        return com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow.class;
    }

}

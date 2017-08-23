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
    public SequenceFlowBehavior createTransitionProvider(PvmTransition runtimeTransition) {
        return new SequenceFlowBehavior(this.extensionPointRegistry, runtimeTransition);
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow.class;
    }

}

package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.SequenceFlow;


public class SequenceFlowProviderFactory implements TransitionProviderFactory<SequenceFlow> {

    @Override
    public SequenceFlowProvider createTransitionProvider(RuntimeTransition runtimeTransition) {
        return new SequenceFlowProvider(runtimeTransition);
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    } 
    
    
}

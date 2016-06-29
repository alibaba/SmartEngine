package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProviderFactory implements TransitionProviderFactory<SmartSequenceFlow> {

    @Override
    public SmartSequenceFlowProvider createTransitionProvider(PvmTransition runtimeTransition) {
        return new SmartSequenceFlowProvider(runtimeTransition);
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}

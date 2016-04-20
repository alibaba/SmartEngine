package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.provider.factory.TransitionProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProviderFactory implements TransitionProviderFactory<SmartSequenceFlow> {

    @Override
    public SmartSequenceFlowProvider createTransitionProvider(RuntimeTransition runtimeTransition) {
        return new SmartSequenceFlowProvider(runtimeTransition);
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}

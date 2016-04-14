package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProvider;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProviderFactory;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProviderFactory implements SequenceFlowProviderFactory<SmartSequenceFlow>{

    @Override
    public SmartSequenceFlowProvider createSequenceFlowProvider(RuntimeSequenceFlow sequenceFlow) {
        return new SmartSequenceFlowProvider(sequenceFlow);
    }

    @Override
    public Class<SmartSequenceFlow> getModelType() {
        return SmartSequenceFlow.class;
    }
}

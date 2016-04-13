package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.assembly.SequenceFlow;

/**
 * Created by ettear on 16-4-11.
 */
public interface SequenceFlowProviderFactory<M> extends ProviderFactory<M> {

    SequenceFlowProvider createSequenceFlowProvider(SequenceFlow sequenceFlow);

}

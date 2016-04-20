package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.assembly.SequenceFlow;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;

/**
 * Created by ettear on 16-4-11.
 */
public interface SequenceFlowProviderFactory<M extends SequenceFlow> extends ProviderFactory<M> {

    SequenceFlowProvider<M> createSequenceFlowProvider(RuntimeSequenceFlow sequenceFlow);

}

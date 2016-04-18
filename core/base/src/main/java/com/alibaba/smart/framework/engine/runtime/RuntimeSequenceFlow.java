package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.SequenceFlow;

/**
 * Created by ettear on 16-4-13.
 */
public interface RuntimeSequenceFlow extends RuntimeInvocable<SequenceFlow> {

    RuntimeActivity getSource();

    RuntimeActivity getTarget();

}

package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.SequenceFlow;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeSequenceFlow extends AbstractRuntimeInvocable<SequenceFlow> implements RuntimeSequenceFlow {
    private RuntimeActivity      source;
    private RuntimeActivity      target;
}

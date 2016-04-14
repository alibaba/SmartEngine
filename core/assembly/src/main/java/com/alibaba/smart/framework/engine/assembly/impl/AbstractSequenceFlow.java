package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.SequenceFlow;
import lombok.Data;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public abstract class AbstractSequenceFlow extends AbstractInvocable implements SequenceFlow {
    private String sourceRef;
    private String targetRef;
}

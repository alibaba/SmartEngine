package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Transition;
import lombok.Data;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public abstract class AbstractTransition extends AbstractInvocable implements Transition {
    private String sourceRef;
    private String targetRef;
}

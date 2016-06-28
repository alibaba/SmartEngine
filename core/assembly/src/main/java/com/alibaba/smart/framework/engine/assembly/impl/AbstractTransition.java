package com.alibaba.smart.framework.engine.assembly.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.Transition;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTransition extends AbstractInvocable implements Transition {

    private String sourceRef;
    private String targetRef;
}

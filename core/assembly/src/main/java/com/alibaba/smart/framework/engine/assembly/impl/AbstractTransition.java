package com.alibaba.smart.framework.engine.assembly.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.assembly.Transition;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTransition extends AbstractActivity implements Transition {
    //FIXME AbstractTransition 这个借口有点怪,但是
    private String sourceRef;
    private String targetRef;
}

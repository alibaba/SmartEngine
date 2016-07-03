package com.alibaba.smart.framework.engine.assembly.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.model.artifact.Transition;

/**
 * Created by ettear on 16-4-13.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTransition extends AbstractBase implements Transition {

    private static final long serialVersionUID = -3833522271165082836L;
    private String sourceRef;
    private String targetRef;
}

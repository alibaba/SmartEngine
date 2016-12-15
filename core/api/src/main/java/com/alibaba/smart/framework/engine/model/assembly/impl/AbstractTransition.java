package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.Transition;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTransition extends AbstractElement implements Transition {

    private static final long serialVersionUID = -3833522271165082836L;
    private String sourceRef;
    private String targetRef;
}

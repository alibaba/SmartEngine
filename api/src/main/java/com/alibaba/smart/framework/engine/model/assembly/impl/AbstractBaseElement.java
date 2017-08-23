package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractBaseElement implements BaseElement {

    private static final long serialVersionUID = -8729383608303781741L;
    private boolean unresolved = true;

    @Override
    public boolean isUnresolved() {
        return unresolved;
    }

    @Override
    public void setUnresolved(boolean unresolved) {
        this.unresolved = unresolved;
    }
}

package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.BaseElement;

/**
 * Created by ettear on 16-4-12.
 */
public abstract class AbstractBase implements BaseElement {

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

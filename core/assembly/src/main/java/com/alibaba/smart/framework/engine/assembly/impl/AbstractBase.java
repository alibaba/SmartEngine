package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Base;

/**
 * Created by ettear on 16-4-12.
 */
public abstract class AbstractBase implements Base {

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

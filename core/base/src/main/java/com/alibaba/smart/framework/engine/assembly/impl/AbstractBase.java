package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.model.artifact.BaseElement;
import com.alibaba.smart.framework.engine.model.artifact.Indentity;

/**
 * Created by ettear on 16-4-12.
 */
public abstract class AbstractBase implements BaseElement,Indentity {

    private static final long serialVersionUID = -8729383608303781741L;
    private boolean unresolved = true;
    private String id;

    @Override
    public boolean isUnresolved() {
        return unresolved;
    }

    @Override
    public void setUnresolved(boolean unresolved) {
        this.unresolved = unresolved;
    }
    
   

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Invocable;

/**
 * Abstract Executor Created by ettear on 16-4-13.
 */
public abstract class AbstractInvocable extends AbstractBase implements Invocable {

    private static final long serialVersionUID = -994693481561308817L;
    private String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

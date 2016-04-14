package com.alibaba.smart.framework.engine.assembly.impl;

import com.alibaba.smart.framework.engine.assembly.Invocable;
import lombok.Data;

/**
 * Abstract Executor
 * Created by ettear on 16-4-13.
 */
public abstract class AbstractInvocable extends AbstractBase implements Invocable {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

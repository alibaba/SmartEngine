package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.Performable;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class AbstractPerformable extends AbstractBaseElement implements Performable {
    private String action;

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }
}

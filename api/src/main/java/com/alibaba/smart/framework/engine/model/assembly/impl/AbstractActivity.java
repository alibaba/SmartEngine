package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.Activity;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivity extends AbstractBase implements Activity {

    private static final long serialVersionUID = 3558917263151685441L;
    private boolean startActivity;

    @Override
    public boolean isStartActivity() {
        return startActivity;
    }

    public void setStartActivity(boolean startActivity) {
        this.startActivity = startActivity;
    }
}
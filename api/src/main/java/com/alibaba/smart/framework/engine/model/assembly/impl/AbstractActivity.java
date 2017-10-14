package com.alibaba.smart.framework.engine.model.assembly.impl;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivity extends AbstractElement implements Activity {

    private static final long serialVersionUID = 3558917263151685441L;

    private boolean startActivity;

    private ExecutePolicy executePolicy;

    @Override
    public boolean isStartActivity() {
        return startActivity;
    }

    public void setStartActivity(boolean startActivity) {
        this.startActivity = startActivity;
    }

    @Override
    public ExecutePolicy getExecutePolicy() {
        return executePolicy;
    }

    @Override
    public void setExecutePolicy(ExecutePolicy executePolicy) {
        this.executePolicy = executePolicy;
    }
}

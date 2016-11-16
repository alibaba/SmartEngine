package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehavior<T extends Activity> implements ActivityBehavior<T> {

    private PvmActivity runtimeActivity;

    public AbstractActivityBehavior(PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }


    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }


}

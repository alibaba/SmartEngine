package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;


/**
 * @author 高海军 帝奇  2016.11.11 TODO 职责略不清晰
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivityBehaviorProvider<T extends Activity> implements ActivityBehavior<T> {

    private PvmActivity runtimeActivity;

    public AbstractActivityBehaviorProvider(PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }





    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }


}

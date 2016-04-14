package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartActivity;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartActivityProvider implements ActivityProvider<SmartActivity> {

    private RuntimeActivity activity;

    public SmartActivityProvider(RuntimeActivity activity){
        this.activity=activity;
    }

    @Override
    public Invoker createInvoker(String event) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

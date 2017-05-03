package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.impl.NotSubsbandMessage;
import com.alibaba.smart.framework.engine.invocation.message.impl.SubsbandMessage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * 抽象Activity Provider实现 Created by ettear on 16-4-20. TODO 职责略不清晰
 */
public class AbstractActivityProvider<T extends Activity> implements ActivityProvider<T> {

    private PvmActivity runtimeActivity;

    public AbstractActivityProvider(PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Invoker createInvoker(String event) {
        if (PvmEventConstant.ACTIVITY_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (PvmEventConstant.ACTIVITY_EXECUTE.name().equals(event)) {
            return this.createExecuteInvoker();
        } else if (PvmEventConstant.ACTIVITY_END.name().equals(event)) {
            return this.createEndInvoker();
        } else if (PvmEventConstant.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return this.createTransitionSelectInvoker();
        } else if (PvmEventConstant.PROCESS_PUSH.name().equals(event)) {

            return this.pushInvoker();


        } else if (null == event){
            return this.createCustomInvoker(runtimeActivity);
        } else {
            return this.createEventInvoker(event);
        }
    }

    protected Invoker pushInvoker() {
        return context -> new NotSubsbandMessage();
    }

    public Invoker createCustomInvoker(PvmActivity runtimeActivity) {
        return null;
    }

    protected Invoker createTransitionSelectInvoker() {
        return null;
    }

    protected Invoker createStartInvoker() {
        return null;
    }

    protected Invoker createExecuteInvoker() {
        return null;
    }

    protected Invoker createEndInvoker() {
        return null;
    }

    protected Invoker createEventInvoker(String event) {
        return null;
    }

    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }
}

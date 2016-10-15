package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * 抽象Activity Provider实现 Created by ettear on 16-4-20. TODO 职责略不清晰
 */
public   class AbstractActivityBehaviorProvider<T extends Activity> implements ActivityBehaviorProvider<T> {

    private PvmActivity runtimeActivity;

    public AbstractActivityBehaviorProvider(PvmActivity runtimeActivity) {
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
        }else if (null == event){
            //TODO 太恶心了.
            return this.createCustomInvoker(runtimeActivity);
        }
        else {
            return this.createEventInvoker(event);
        }
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

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {

    }
}

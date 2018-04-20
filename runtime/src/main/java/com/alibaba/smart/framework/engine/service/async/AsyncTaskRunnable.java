package com.alibaba.smart.framework.engine.service.async;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by niefeng on 2018/4/18.
 */
public class AsyncTaskRunnable implements Runnable {

    private final PvmTransition transition;
    private final ExecutionContext context;

    public AsyncTaskRunnable(PvmTransition transition, ExecutionContext context) {
        this.transition = transition;
        this.context = context;
    }

    @Override
    public void run() {
        this.transition.execute(context);
    }
}

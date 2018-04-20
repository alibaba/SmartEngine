package com.alibaba.smart.framework.engine.service.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by niefeng on 2018/4/18.
 */
public class AsyncTaskExecutor implements LifeCycleListener {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Override
    public void start() {

    }

    public static void submit(PvmTransition transition, ExecutionContext context){
        executorService.submit(new AsyncTaskRunnable(transition, context));
    }

    @Override
    public void stop() {
        executorService.shutdown();
    }
}

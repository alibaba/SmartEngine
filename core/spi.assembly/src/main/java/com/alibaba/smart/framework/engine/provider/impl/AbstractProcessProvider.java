package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;

/**
 * 抽象Process Provider实现 Created by ettear on 16-4-20.
 */
public class AbstractProcessProvider<T extends Process> implements ActivityProvider<T> {

    private PvmProcess runtimeProcess;

    public AbstractProcessProvider(PvmProcess runtimeProcess) {
        this.runtimeProcess = runtimeProcess;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Invoker createInvoker(String event) {
        if (AtomicOperationEvent.PROCESS_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (AtomicOperationEvent.PROCESS_END.name().equals(event)) {
            return this.createEndInvoker();
        } else {
            return this.createEventInvoker(event);
        }
    }

    protected Invoker createStartInvoker() {
        return null;
    }

    protected Invoker createEndInvoker() {
        return null;
    }

    protected Invoker createEventInvoker(String event) {
        return null;
    }

    protected PvmProcess getRuntimeProcess() {
        return runtimeProcess;
    }
}

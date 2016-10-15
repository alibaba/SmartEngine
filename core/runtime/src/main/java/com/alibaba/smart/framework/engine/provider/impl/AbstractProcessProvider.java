package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.provider.ActivityBehaviorProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * 抽象Process Provider实现 Created by ettear on 16-4-20.
 * TODO 看下存在性
 */
public class AbstractProcessProvider<T extends Process> implements ActivityBehaviorProvider<T> {

    private PvmProcessDefinition runtimeProcess;

    public AbstractProcessProvider(PvmProcessDefinition runtimeProcess) {
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
        if (PvmEventConstant.PROCESS_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (PvmEventConstant.PROCESS_END.name().equals(event)) {
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

    protected PvmProcessDefinition getRuntimeProcess() {
        return runtimeProcess;
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {
        throw new RuntimeException("not impl");
    }
}

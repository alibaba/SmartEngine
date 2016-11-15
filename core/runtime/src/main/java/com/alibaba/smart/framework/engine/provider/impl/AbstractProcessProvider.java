package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Process;
import com.alibaba.smart.framework.engine.provider.ActivityBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;


/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public class AbstractProcessProvider<T extends Process> implements ActivityBehavior<T> {

    private PvmProcessDefinition runtimeProcess;

    public AbstractProcessProvider(PvmProcessDefinition runtimeProcess) {
        this.runtimeProcess = runtimeProcess;
    }


    protected PvmProcessDefinition getRuntimeProcess() {
        return runtimeProcess;
    }

    @Override
    public void execute(PvmActivity runtimeActivity, ExecutionContext context) {
        throw new RuntimeException("not impl");
    }
}

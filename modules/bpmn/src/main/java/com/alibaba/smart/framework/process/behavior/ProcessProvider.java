package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.process.model.bpmn.assembly.Process;


/**
 * @author 高海军 帝奇 Apr 21, 2016 8:28:49 PM
 * TODO 每个xml element 扩展点需要定义3个扩展点,略繁琐
 */
public class ProcessProvider extends AbstractActivityProvider<Process>
        implements ActivityProvider<Process> {

    public ProcessProvider(RuntimeActivity runtimeActivity) {
        super(runtimeActivity);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new DefaultInvoker("Execute activity " + this.getRuntimeActivity().getId());
    }
}

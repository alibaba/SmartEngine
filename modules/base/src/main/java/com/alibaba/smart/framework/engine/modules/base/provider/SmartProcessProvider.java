package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartProcess;
import com.alibaba.smart.framework.engine.modules.base.invocation.SmartInvoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractProcessProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartProcessProvider extends AbstractProcessProvider<SmartProcess> implements ActivityProvider<SmartProcess> {


    public SmartProcessProvider(RuntimeProcess runtimeProcess) {
        super(runtimeProcess);
    }

    @Override
    protected Invoker createStartInvoker() {
        return new SmartInvoker("Start process " + this.getRuntimeProcess().getId());
    }

    @Override
    protected Invoker createEndInvoker() {
        return new SmartInvoker("End process " + this.getRuntimeProcess().getId());
    }
}

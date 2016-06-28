package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartActivity;
import com.alibaba.smart.framework.engine.modules.base.invocation.SmartInvoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.provider.impl.AbstractActivityProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartActivityProvider extends AbstractActivityProvider<SmartActivity> implements ActivityProvider<SmartActivity> {

    public SmartActivityProvider(RuntimeActivity runtimeActivity) {
        super(runtimeActivity);
    }

    @Override
    protected Invoker createExecuteInvoker() {
        return new SmartInvoker("Execute activity " + this.getRuntimeActivity().getId());
    }
}

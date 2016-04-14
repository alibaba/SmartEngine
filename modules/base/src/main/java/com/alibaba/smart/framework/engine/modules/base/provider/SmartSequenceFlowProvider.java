package com.alibaba.smart.framework.engine.modules.base.provider;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.modules.base.assembly.SmartSequenceFlow;
import com.alibaba.smart.framework.engine.provider.SequenceFlowProvider;

/**
 * Created by ettear on 16-4-14.
 */
public class SmartSequenceFlowProvider implements SequenceFlowProvider<SmartSequenceFlow> {

    @Override
    public Invoker createInvoker(String event) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}

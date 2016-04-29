package com.alibaba.smart.framework.engine.modules.mvel;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * Created by ettear on 16-4-29.
 */
public class MvelInvoker implements Invoker{

    @Override
    public Message invoke(InstanceContext context) {
        System.out.printf("Invoke mvel '"+ context+"'");
        return null;
    }
}

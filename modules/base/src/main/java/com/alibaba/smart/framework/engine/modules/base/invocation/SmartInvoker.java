package com.alibaba.smart.framework.engine.modules.base.invocation;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * Created by ettear on 16-4-19.
 */
public class SmartInvoker implements Invoker {
    private String message;
    public SmartInvoker(String message){
        this.message=message;
    }

    @Override
    public Message invoke(InstanceContext context) {
        System.out.println(message);
        return null;
    }
}

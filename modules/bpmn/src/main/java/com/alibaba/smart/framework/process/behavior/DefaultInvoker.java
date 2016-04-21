package com.alibaba.smart.framework.process.behavior;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;


public class DefaultInvoker implements Invoker {
    private String message;
    public DefaultInvoker(String message){
        this.message=message;
    }

    @Override
    public Message invoke(InstanceContext context) {
        System.out.println(message);
        return null;
    }
}

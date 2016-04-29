package com.alibaba.smart.framework.engine.modules.groovy;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * Created by ettear on 16-4-29.
 */
public class GroovyInvoker implements Invoker {

    private String script;
    public GroovyInvoker(String script){
        this.script=script;
    }
    @Override
    public Message invoke(InstanceContext context) {
        System.out.println("Invoke groovy '"+ script+"'");
        return null;
    }
}

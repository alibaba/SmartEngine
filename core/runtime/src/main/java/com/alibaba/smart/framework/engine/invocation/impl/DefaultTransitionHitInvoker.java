package com.alibaba.smart.framework.engine.invocation.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultTransitionHitInvoker implements Invoker {

    public final static DefaultTransitionHitInvoker instance = new DefaultTransitionHitInvoker();

    private DefaultTransitionHitInvoker() {
    }

    @Override
    public Message invoke(ExecutionContext context) {
        Message message = new DefaultMessage();
        message.setBody(true);
        return message;
    }
}

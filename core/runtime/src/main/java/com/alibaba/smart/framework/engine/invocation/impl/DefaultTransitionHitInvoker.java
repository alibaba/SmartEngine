package com.alibaba.smart.framework.engine.invocation.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.invocation.message.impl.DefaultMessage;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
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

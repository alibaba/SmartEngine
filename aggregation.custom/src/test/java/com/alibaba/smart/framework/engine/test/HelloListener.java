package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.Listener;
import com.alibaba.smart.framework.engine.test.cases.extensions.MultiValueAndHelloListenerTest;

public class HelloListener implements Listener {
    @Override
    public void execute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("hello");
        MultiValueAndHelloListenerTest.trace.add(text);
    }
}

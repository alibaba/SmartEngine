package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;


public class EventListener implements JavaDelegation {
    @Override
    public Object execute(ExecutionContext executionContext) {
        String text= (String)executionContext.getRequest().get("hello");
        MultiValueAndEventListenerTest.trace.add(text);
        return text;
    }
}

package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.listener.EventListener;
import com.alibaba.smart.framework.engine.test.cases.ExecutionListenerAndValueTest;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class Listener implements EventListener {

    @Override
    public void execute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");
        ExecutionListenerAndValueTest.trace.add("Listener: " + text);
    }
}

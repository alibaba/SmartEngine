package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.test.cases.ExecutionListenerAndValueTest;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class Tracker implements JavaDelegation {
    @Override
    public void execute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("hello");
        executionContext.getResponse().putAll(executionContext.getRequest());
    }
}

package com.alibaba.smart.framework.engine.modules.smart;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class Tracker implements JavaDelegation {
    @Override
    public Object execute(ExecutionContext executionContext) {
        String text= (String)executionContext.getRequest().get("text");
        ExecutionListenerAndValueTest.trace.add(text);
        return text;
    }
}

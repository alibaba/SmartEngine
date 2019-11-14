package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TccTracker implements JavaDelegation {
    public TccTracker() {
        String text = "";

    }

    @Override
    public void execute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");

        executionContext.getResponse().put("hello1",text);

    }


}

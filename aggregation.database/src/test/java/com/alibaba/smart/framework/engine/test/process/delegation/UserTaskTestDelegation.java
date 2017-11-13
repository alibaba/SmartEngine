package com.alibaba.smart.framework.engine.test.process.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.test.process.FullMultiInstanceTest;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class UserTaskTestDelegation implements JavaDelegation {


    @Override
    public Object execute(ExecutionContext executionContext) {
        String text= (String)executionContext.getRequest().get("text");
        FullMultiInstanceTest.trace.add(text);
        return text;
    }
}

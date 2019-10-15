package com.alibaba.smart.framework.engine.test.delegation;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.test.cases.ExecutionListenerAndValueTest;

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

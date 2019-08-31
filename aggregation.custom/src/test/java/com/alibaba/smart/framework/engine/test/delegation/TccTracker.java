package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.test.cases.ExecutionListenerAndValueTest;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TccTracker implements TccDelegation {
    public TccTracker() {
        String text = "";

    }

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");
        ExecutionListenerAndValueTest.trace.add(text);
        return TccResult.buildSucessfulResult(null);
    }

    @Override
    public TccResult confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult cancelExecute(ExecutionContext executionContext) {
        return null;
    }
}

package com.alibaba.smart.framework.engine.test.storage.dual.helper;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;

public class BasicServiceTaskDelegation implements TccDelegation {

    private static final AtomicLong counter = new AtomicLong(0);

    public static Long getCounter() {
        return counter.get();
    }

    public static void resetCounter() {
        counter.set(0L);
    }

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        counter.addAndGet(1);
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

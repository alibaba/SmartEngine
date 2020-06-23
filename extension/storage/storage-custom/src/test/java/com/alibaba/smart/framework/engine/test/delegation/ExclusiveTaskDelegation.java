package com.alibaba.smart.framework.engine.test.delegation;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusiveTaskDelegation implements TccDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExclusiveTaskDelegation.class);

    private static final AtomicLong counter = new AtomicLong(100);

    public static Long getCounter() {
        return counter.get();
    }

    public static void resetCounter() {
        counter.set(100L);
    }

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getActivityInstances();
        LOGGER.info("TCC executing: invoke some hsf code stuff" + executionContext.getRequest());
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

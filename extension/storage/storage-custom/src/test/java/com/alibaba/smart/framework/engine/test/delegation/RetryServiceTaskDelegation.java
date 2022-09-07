package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.annoation.Retryable;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Retryable
public class RetryServiceTaskDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryServiceTaskDelegation.class);

    private static final AtomicLong counter = new AtomicLong(1);

    public static Long getCounter() {
        return counter.get();
    }

    public void execute(ExecutionContext executionContext) {
        LOGGER.info("TCC executing: invoke some hsf code stuff" + executionContext.getRequest());
        counter.addAndGet(1);
        throw new RuntimeException("retry failed");
    }



}

package com.alibaba.smart.framework.engine.retry;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public class RetryDelegation implements JavaDelegation {

    private AtomicInteger retryTimes = new AtomicInteger(0);

    @Override
    public void execute(ExecutionContext executionContext) {

        if (retryTimes.getAndIncrement() < 1) {
            throw new RuntimeException("Execute occurs error.");
        }
    }
}

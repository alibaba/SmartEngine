package com.alibaba.smart.framework.engine.retry;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
public class RetryDelegation implements JavaDelegation {

    private AtomicInteger retryTimes = new AtomicInteger(0);

    @Override
    public Object execute(ExecutionContext executionContext) {
        if (retryTimes.getAndIncrement() < 1) {
            throw new RuntimeException("Execute occurs error.");
        }
        return null;
    }
}

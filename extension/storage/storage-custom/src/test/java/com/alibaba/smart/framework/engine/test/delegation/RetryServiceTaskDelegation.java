package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.annotation.Retryable;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
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
        LOGGER.info("executing  RPC service " + executionContext.getRequest());
        counter.addAndGet(1);
        Object action = executionContext.getRequest().get("action");

        if("retry_all_failed".equals(action)){
            throw new RuntimeException("retry_all_failed");
        }else if ("retry_twice".equals(action)){
            if(executionContext.getResponse().get("count").equals(2)){
                // business logic
                ThreadPoolUtil.sleepSilently(20);
            }else {
                throw new RuntimeException("retry_twice");
            }
        }

    }



}

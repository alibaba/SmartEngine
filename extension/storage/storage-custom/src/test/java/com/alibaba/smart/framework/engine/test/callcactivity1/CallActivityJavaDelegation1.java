package com.alibaba.smart.framework.engine.test.callcactivity1;

import com.alibaba.smart.framework.engine.annotation.Retryable;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Retryable(delay = 2000, maxAttempts = 1)
public class CallActivityJavaDelegation1 implements JavaDelegation {

    public CallActivityJavaDelegation1(){
        logger.info("CallActivityJavaDelegation1 init");
    }

    static Logger logger = LoggerFactory.getLogger(CallActivityJavaDelegation1.class);
    @Override
    public void execute(ExecutionContext executionContext) {
        logger.info("==> test111");
    }
}

package com.alibaba.smart.framework.engine.test.callcactivity1;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallActivityJavaDelegation3 implements JavaDelegation {
    Logger logger = LoggerFactory.getLogger(CallActivityJavaDelegation3.class);
    @Override
    public void execute(ExecutionContext executionContext) {
        logger.info("==> test 333");
    }
}

package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThrowExceptionTaskDelegation implements TccDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowExceptionTaskDelegation.class);

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {

       throw new RuntimeException("test exception");

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

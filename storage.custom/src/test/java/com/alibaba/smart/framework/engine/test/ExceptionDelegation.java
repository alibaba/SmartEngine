package com.alibaba.smart.framework.engine.test;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionDelegation implements TccDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionDelegation.class);



    private static  final AtomicLong counter= new AtomicLong();

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        throw new RuntimeException("should fail");
    }

    public static Long getCounter() {
        return counter.get();
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

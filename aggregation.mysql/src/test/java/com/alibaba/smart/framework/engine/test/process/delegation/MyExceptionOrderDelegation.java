package com.alibaba.smart.framework.engine.test.process.delegation;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyExceptionOrderDelegation implements TccDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(MyExceptionOrderDelegation.class);



    private static  final AtomicLong counter= new AtomicLong();

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        throw new IllegalArgumentException("yahaha");
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

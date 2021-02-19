package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.LockException;

import org.apache.commons.math3.exception.util.ExceptionContext;

public class DoNothingLockStrategy implements LockStrategy {


    @Override
    public void tryLock(String processInstanceId, ExecutionContext context) throws LockException {
        //do nothing

    }

    @Override
    public void unLock(String processInstanceId, ExecutionContext context) throws LockException {
        //do nothing
    }
}

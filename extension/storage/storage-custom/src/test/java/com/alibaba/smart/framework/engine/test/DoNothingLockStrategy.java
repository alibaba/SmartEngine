package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.exception.LockException;



public class DoNothingLockStrategy implements LockStrategy {


    @Override
    public void tryLock(String processInstanceId) throws LockException {
        //do nothing

    }

    @Override
    public void unLock(String processInstanceId) throws LockException {
        //do nothing
    }
}

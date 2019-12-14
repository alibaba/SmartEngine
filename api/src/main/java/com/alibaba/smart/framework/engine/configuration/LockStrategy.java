package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.exception.LockException;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  06:20.
 */
public interface LockStrategy {


    void tryLock(String processInstanceId) throws LockException;

    void unLock(String processInstanceId) throws LockException;
}

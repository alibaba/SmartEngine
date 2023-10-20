package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.exception.LockException;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  06:20.
 * @Deprecated The granularity of the LockStrategy is too small,should NOT use anymore.
 */
public interface LockStrategy {


    void tryLock(String processInstanceId, ExecutionContext context) throws LockException;

    void unLock(String processInstanceId, ExecutionContext context) throws LockException;
}

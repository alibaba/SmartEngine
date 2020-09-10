package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.exception.EngineException;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-10 16:11.
 */
public class DefaultExceptionProcessor implements ExceptionProcessor {
    @Override
    public void process(Exception exception, Object context) {
        throw new EngineException(exception);
    }
}
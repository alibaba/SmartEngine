package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  11:13.
 */
public class CustomExceptioinProcessor implements ExceptionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptioinProcessor.class);

    @Override
    public void process(Exception exception,Object context) {
        LOGGER.error(exception.getMessage(),exception);

    }
}

package com.alibaba.smart.framework.example;

import com.alibaba.smart.framework.engine.common.processor.ExceptionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  11:13.
 */
public class CustomExceptioinProcessor implements ExceptionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptioinProcessor.class);

    @Override
    public void process(Exception exception) {
        LOGGER.error(exception.getMessage(),exception);

//        throw  new RuntimeException(exception);

    }
}

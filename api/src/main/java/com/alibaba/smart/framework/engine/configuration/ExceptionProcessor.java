package com.alibaba.smart.framework.engine.configuration;

/**
 *
 */
public interface ExceptionProcessor {

    void process(Exception exception,Object context);

}

package com.alibaba.smart.framework.engine.configuration;


/**
 * Created by 高海军 帝奇 74394 on 2016 December  16:05.
 */
public interface ExceptionProcessor {

    void process(Exception exception,Object context);

}

package com.alibaba.smart.framework.engine;

/**
 * 引擎异常
 * Created by ettear on 16-4-12.
 */
public class EngineException extends Exception {
    public EngineException(String message){
        super(message);
    }

    public EngineException(String message,Exception e){
        super(message,e);
    }
}

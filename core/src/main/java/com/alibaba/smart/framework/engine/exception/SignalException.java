package com.alibaba.smart.framework.engine.exception;


/**
 * @author 高海军 帝奇  2020.9.9
 */
public class SignalException extends EngineException {


    private static final long serialVersionUID = -5576514678329147766L;

    public SignalException(String message) {
        super(message);
    }

    public SignalException(String message, Exception e) {
        super(message, e);
    }
}

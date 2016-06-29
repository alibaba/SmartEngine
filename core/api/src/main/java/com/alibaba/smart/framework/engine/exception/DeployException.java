package com.alibaba.smart.framework.engine.exception;

/**
 * Created by ettear on 16-4-13.
 */
public class DeployException extends EngineException {

    
    private static final long serialVersionUID = -5576514678329147766L;

    public DeployException(String message) {
        super(message);
    }

    public DeployException(String message, Exception e) {
        super(message, e);
    }
}

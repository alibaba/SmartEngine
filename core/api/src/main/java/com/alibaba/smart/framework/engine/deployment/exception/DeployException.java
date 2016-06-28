package com.alibaba.smart.framework.engine.deployment.exception;

/**
 * Created by ettear on 16-4-13.
 */
public class DeployException extends Exception {

    
    private static final long serialVersionUID = -5576514678329147766L;

    public DeployException(String message) {
        super(message);
    }

    public DeployException(String message, Exception e) {
        super(message, e);
    }
}

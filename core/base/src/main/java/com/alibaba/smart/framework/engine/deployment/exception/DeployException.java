package com.alibaba.smart.framework.engine.deployment.exception;

/**
 * Created by ettear on 16-4-13.
 */
public class DeployException extends Exception {
    public DeployException(String message){
        super(message);
    }

    public DeployException(String message,Exception e){
        super(message,e);
    }
}

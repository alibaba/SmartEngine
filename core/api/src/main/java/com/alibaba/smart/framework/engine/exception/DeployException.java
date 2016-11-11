package com.alibaba.smart.framework.engine.exception;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
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

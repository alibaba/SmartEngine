package com.alibaba.smart.framework.engine.exception;

/**
 * Created by niefeng on 2018/4/18.
 */
public class NoSatisfyTargetInExclusiveGatewayException extends RuntimeException {
    public NoSatisfyTargetInExclusiveGatewayException() {
    }

    public NoSatisfyTargetInExclusiveGatewayException(String message) {
        super(message);
    }

    public NoSatisfyTargetInExclusiveGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSatisfyTargetInExclusiveGatewayException(Throwable cause) {
        super(cause);
    }

}

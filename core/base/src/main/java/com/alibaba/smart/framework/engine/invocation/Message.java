package com.alibaba.smart.framework.engine.invocation;

/**
 * Created by ettear on 16-4-11.
 */
public interface Message {
    <T> T getBody();

    <T> void setBody(T body);

    boolean isFault();

    boolean isSuspend();
}

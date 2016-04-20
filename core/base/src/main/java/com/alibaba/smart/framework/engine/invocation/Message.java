package com.alibaba.smart.framework.engine.invocation;

/**
 * Message
 * Created by ettear on 16-4-11.
 */
public interface Message {
    <T> T getBody();

    <T> void setBody(T body);

    boolean isFault();

    void setFault(boolean fault);

    boolean isSuspend();

    void setSuspend(boolean suspend);

}

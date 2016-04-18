package com.alibaba.smart.framework.engine.invocation;

/**
 * Created by ettear on 16-4-11.
 */
public interface Message {
    <T> T getBody();

    <T> void setBody(T body);

    Object getMessageID();

    void setMessageID(Object messageID);

    boolean isFault();

    <T> void setFaultBody(T faultBody);
}

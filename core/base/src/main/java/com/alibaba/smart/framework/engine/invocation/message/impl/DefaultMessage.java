package com.alibaba.smart.framework.engine.invocation.message.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.invocation.message.Message;

/**
 * Created by ettear on 16-4-19.
 */
@Data
public class DefaultMessage implements Message {

    private Object  body;
    private boolean fault;
    private boolean suspend;
    
}

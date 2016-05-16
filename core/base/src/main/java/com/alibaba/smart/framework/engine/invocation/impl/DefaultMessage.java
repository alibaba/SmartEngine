package com.alibaba.smart.framework.engine.invocation.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * Created by ettear on 16-4-19.
 */
@Data
public class DefaultMessage implements Message {
    private Object body;
    private boolean fault;
    private boolean suspend;
}

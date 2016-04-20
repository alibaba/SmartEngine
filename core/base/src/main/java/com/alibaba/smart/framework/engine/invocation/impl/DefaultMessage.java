package com.alibaba.smart.framework.engine.invocation.impl;

import com.alibaba.smart.framework.engine.invocation.Message;
import lombok.Data;

/**
 * Created by ettear on 16-4-19.
 */
@Data
public class DefaultMessage implements Message {
    private Object body;
    private boolean fault;
    private boolean suspend;
}

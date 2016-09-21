package com.alibaba.smart.framework.engine.invocation.message.impl;

/**
 * 暂停
 * Created by dongdongzdd on 16/9/20.
 */
public class SubsbandMessage extends DefaultMessage {

    public SubsbandMessage() {
        this.setSuspend(true);
    }
}

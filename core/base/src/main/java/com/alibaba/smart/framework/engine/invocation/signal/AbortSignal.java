package com.alibaba.smart.framework.engine.invocation.signal;

/**
 * @author dongdong.zdd
 * @since 2016-12-13
 */
public class AbortSignal extends Signal {

    public AbortSignal() {
    }

    public AbortSignal(String message) {
        super(message);
    }

    @Override
    protected String getSignal() {
        return "abort";
    }


}

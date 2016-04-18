package com.alibaba.smart.framework.engine.invocation.impl;

import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * DoNothingInvoker
 * Created by ettear on 16-4-14.
 */
public class DoNothingInvoker implements Invoker {

    public final static DoNothingInvoker instance=new DoNothingInvoker();

    private DoNothingInvoker(){}

    @Override
    public Message invoke(Context context) {
        return null;
    }
}

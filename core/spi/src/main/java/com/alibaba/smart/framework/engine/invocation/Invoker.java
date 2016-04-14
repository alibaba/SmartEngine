package com.alibaba.smart.framework.engine.invocation;

import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.impl.DoNothingInvoker;

/**
 * Created by ettear on 16-4-11.
 */
public interface Invoker {

    public final static Invoker DO_NOTHING_INVOKER = new DoNothingInvoker();

    Message invoke(Context context);
}

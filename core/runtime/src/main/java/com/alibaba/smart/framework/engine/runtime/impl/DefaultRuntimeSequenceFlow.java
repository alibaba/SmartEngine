package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractSequenceFlow;
import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeSequenceFlow;
import lombok.Data;

/**
 * Created by ettear on 16-4-13.
 */
@Data
public class DefaultRuntimeSequenceFlow extends AbstractSequenceFlow implements RuntimeSequenceFlow {

    private Class<?>        modelType;
    private Invoker         invoker;
    private RuntimeActivity source;
    private RuntimeActivity target;

    @Override
    public Message invoke(Context context) {
        return invoker.invoke(context);
    }

    @Override
    public Message invokeAsync(Context context) {
        return invoker.invoke(context);
    }

}

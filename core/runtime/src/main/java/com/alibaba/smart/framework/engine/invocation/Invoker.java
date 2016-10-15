package com.alibaba.smart.framework.engine.invocation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;

/**
 * Invoker Created by ettear on 16-4-11.
 */
public interface Invoker {
    //TODO 考虑删除,移除 Invoker 和  减少对象Message创建开销。保持 gc 稳定。
    Message invoke(ExecutionContext context);
}

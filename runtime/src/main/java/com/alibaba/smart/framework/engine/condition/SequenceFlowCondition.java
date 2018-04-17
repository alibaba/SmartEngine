package com.alibaba.smart.framework.engine.condition;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * Created by niefeng on 2018/4/17.
 */
public interface SequenceFlowCondition {
    boolean check(ExecutionContext executionContext);
}

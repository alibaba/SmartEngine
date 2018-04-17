package com.alibaba.smart.framework.engine.test.condition;

import com.alibaba.smart.framework.engine.condition.SequenceFlowCondition;
import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * Created by niefeng on 2018/4/17.
 */
public class TestCondition implements SequenceFlowCondition {
    @Override
    public boolean check(ExecutionContext executionContext) {
        return true;
    }
}

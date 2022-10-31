package com.alibaba.smart.framework.engine.test.parallelgateway.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

public class ChildReceiveTaskDelegation implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        ChildServiceTaskDelegation.counter.addAndGet(5);
        System.out.println("ChildReceiveTaskDelegation");
    }



}

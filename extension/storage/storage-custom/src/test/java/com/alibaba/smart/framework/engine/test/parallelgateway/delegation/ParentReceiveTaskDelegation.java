package com.alibaba.smart.framework.engine.test.parallelgateway.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

public class ParentReceiveTaskDelegation implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        ChildServiceTaskDelegation.counter.addAndGet(7);
        System.out.println("ParentReceiveTaskDelegation");

    }



}

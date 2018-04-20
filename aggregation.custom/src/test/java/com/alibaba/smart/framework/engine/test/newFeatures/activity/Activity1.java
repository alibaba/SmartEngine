package com.alibaba.smart.framework.engine.test.newFeatures.activity;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

/**
 * Created by niefeng on 2018/4/19.
 */
public class Activity1 implements JavaDelegation {
    @Override
    public Object execute(ExecutionContext executionContext) {
        System.out.println("this is "+this.getClass().getSimpleName());
        return null;
    }
}

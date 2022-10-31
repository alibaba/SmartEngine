package com.alibaba.smart.framework.engine.test.parallelgateway.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ChildServiceTaskDelegation implements JavaDelegation {


    public static final AtomicLong counter = new AtomicLong(1);

    public static Long getCounter() {
        return counter.get();
    }

    @Override
    public void execute(ExecutionContext executionContext) {
        counter.addAndGet(1);
        System.out.println("ChildServiceTaskDelegation");

    }



}

package com.alibaba.smart.framework.engine.test.process.delegation;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataBaseCreateOrderDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseCreateOrderDelegation.class);

    private static final AtomicLong counter = new AtomicLong();

    public static Long getCounter() {
        return counter.get();
    }

    public void execute(ExecutionContext executionContext) {
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getActivityInstances();
        LOGGER.info("TCC executing: invoke some hsf code stuff" + executionContext.getRequest());
        counter.addAndGet(1);
    }



}

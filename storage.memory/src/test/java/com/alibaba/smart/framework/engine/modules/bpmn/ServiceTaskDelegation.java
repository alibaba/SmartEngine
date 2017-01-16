package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class ServiceTaskDelegation implements TccDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);



    private static  final AtomicLong counter= new AtomicLong();

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
//         LOGGER.info(executionContext.getRequest().toString());
//         LOGGER.info("TCC executing: "+executionContext.getCurrentExecution().toString());
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getNewActivityInstances();
        LOGGER.info("TCC executing: " + activityInstances.get(activityInstances.size() - 1));
        counter.addAndGet(1);
        return null;
    }

    public static Long getCounter() {
        return counter.get();
    }

    @Override
    public TccResult confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult cancelExecute(ExecutionContext executionContext) {
        return null;
    }

}

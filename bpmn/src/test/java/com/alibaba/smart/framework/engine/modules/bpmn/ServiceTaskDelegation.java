package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ServiceTaskDelegation implements TccDelegation<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);

    @Override
    public TccResult<Object> tryExecute(ExecutionContext executionContext) {
//         LOGGER.info(executionContext.getRequest().toString());
//         LOGGER.info("TCC executing: "+executionContext.getCurrentExecution().toString());
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getActivityInstances();
        LOGGER.info("TCC executing: " + activityInstances.get(activityInstances.size() - 1));

        return null;
    }

    @Override
    public TccResult<Object> confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult<Object> cancelExecute(ExecutionContext executionContext) {
        return null;
    }

}

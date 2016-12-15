package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AuditProcessServiceTaskDelegation implements TccDelegation  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditProcessServiceTaskDelegation.class);

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
//         LOGGER.info(executionContext.getRequest().toString());
//         LOGGER.info("TCC executing: "+executionContext.getCurrentExecution().toString());
        List<ActivityInstance> activityInstances = executionContext.getProcessInstance().getNewActivityInstances();
        LOGGER.info("TCC executing: " + activityInstances.get(activityInstances.size() - 1));

        return null;
    }

    @Override
    public TccResult  confirmExecute(ExecutionContext executionContext) {
        return null;
    }

    @Override
    public TccResult cancelExecute(ExecutionContext executionContext) {
        return null;
    }

}

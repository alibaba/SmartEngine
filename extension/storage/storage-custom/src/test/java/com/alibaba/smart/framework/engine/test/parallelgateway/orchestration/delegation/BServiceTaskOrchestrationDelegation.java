package com.alibaba.smart.framework.engine.test.parallelgateway.orchestration.delegation;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.exception.EngineException;

import com.alibaba.smart.framework.engine.test.parallelgateway.orchestration.ThreadExecutionResult;
import com.alibaba.smart.framework.engine.test.parallelgateway.single.thread.ServiceTaskDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BServiceTaskOrchestrationDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);


    @Override
    public void execute(ExecutionContext executionContext) {

        Map<String, Object> request = executionContext.getRequest();
        String processDefinitionActivityId = executionContext.getExecutionInstance().getProcessDefinitionActivityId();
        Long sleepTime = (Long)request.get( processDefinitionActivityId);

        long id = Thread.currentThread().getId();
        request.put(processDefinitionActivityId,new ThreadExecutionResult(id,sleepTime));

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new EngineException(e);
        }

    }
}

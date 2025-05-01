package com.alibaba.smart.framework.engine.test.process.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.persister.common.assistant.pojo.ThreadExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Qualifier("com.alibaba.smart.framework.engine.test.process.delegation.ServiceTaskDelegation")
public class ServiceTaskDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);


    @Override
    public void execute(ExecutionContext executionContext) {

        Map<String, Object> request = executionContext.getRequest();
        String processDefinitionActivityId = executionContext.getExecutionInstance().getProcessDefinitionActivityId();
        Object sleepTimeObject = request.get(processDefinitionActivityId);

        if(null != sleepTimeObject){
            Long sleepTime = (Long) sleepTimeObject;

            long id = Thread.currentThread().getId();

            request.put(processDefinitionActivityId,new ThreadExecutionResult(id,sleepTime));

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new EngineException(e);
            }
        }


    }
}

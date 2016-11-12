package com.alibaba.smart.framework.engine.modules.bpmn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;


public class ServiceTaskDelegation  implements TccDelegation<Object>{

    private static final Logger   LOGGER          = LoggerFactory.getLogger(ServiceTaskDelegation.class);

    @Override
    public TccResult<Object> tryExecute(ExecutionContext executionContext) {
//         LOGGER.info(executionContext.getRequest().toString());
//         LOGGER.info("TCC executing: "+executionContext.getCurrentExecution().toString());
        LOGGER.info("TCC executing: ");

        return null;
    }

    @Override
    public TccResult<Object> confirmExecute(ExecutionContext executionContext) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TccResult<Object> cancelExecute(ExecutionContext executionContext) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

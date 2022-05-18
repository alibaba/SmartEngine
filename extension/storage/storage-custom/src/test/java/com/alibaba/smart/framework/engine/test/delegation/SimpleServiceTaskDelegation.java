package com.alibaba.smart.framework.engine.test.delegation;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class SimpleServiceTaskDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServiceTaskDelegation.class);
    public static final String DEFAULT_SEQUENCE_FLOW = "defaultSequenceFlow";


    @Override
    public void execute(ExecutionContext executionContext) {
        executionContext.getResponse().put(DEFAULT_SEQUENCE_FLOW,executionContext.getExecutionInstance().getProcessDefinitionActivityId());
        executionContext.getResponse().putAll(executionContext.getRequest());
    }
}

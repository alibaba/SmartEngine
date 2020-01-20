package com.alibaba.smart.framework.engine.test.delegation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfcMarketPlaceJavaDelegation implements TccDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfcMarketPlaceJavaDelegation.class);
    private static final AtomicLong counter = new AtomicLong();
    private static List<String> holder = new ArrayList<String>();

    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        String processDefinitionActivityId = executionContext.getExecutionInstance().getProcessDefinitionActivityId();
        holder.add(processDefinitionActivityId);
        return TccResult.buildSucessfulResult(null);
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

package com.alibaba.smart.framework.engine.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OfcMarketPlaceJavaDelegation implements TccDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(OfcMarketPlaceJavaDelegation.class);

    private static List<String> holder = new ArrayList<String>();

    private static  final AtomicLong counter= new AtomicLong();

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

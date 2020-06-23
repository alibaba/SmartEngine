package com.alibaba.smart.framework.engine.test.delegation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclusiveGatewayDelegation implements JavaDelegation {

    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();

        if(null == request){
            request = new HashMap<String, Object>();
            executionContext.setRequest(request);

        }

        request.put("route", "a");

    }

}

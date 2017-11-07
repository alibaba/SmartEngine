package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.delegation.TccDelegation;
import com.alibaba.smart.framework.engine.delegation.TccResult;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class ServiceTaskDelegation implements TccDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);

    @Getter
    private static List<String> arrayList = new ArrayList<String>();



    @Override
    public TccResult tryExecute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();
        if(null !=  request){
            Object o = request.get("input");
            if(o != null){
                String input = o.toString();
                arrayList.add(input);
                LOGGER.info("request input"+arrayList);
            }
        }


        return null;
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

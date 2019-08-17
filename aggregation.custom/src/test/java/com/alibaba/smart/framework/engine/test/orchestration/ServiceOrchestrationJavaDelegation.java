package com.alibaba.smart.framework.engine.test.orchestration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceOrchestrationJavaDelegation implements JavaDelegation{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOrchestrationJavaDelegation.class);

    @Getter
    private  static  List<String> arrayList = new ArrayList<String>();

    @Autowired
    private  Service  aService;

    @Override
    public Object execute(ExecutionContext executionContext) {



        Map<String, Object> request = executionContext.getRequest();
            if(null !=  request){
                Object o = request.get("input");
                if(o != null){
                    String input = o.toString();
                    arrayList.add(input);
                    LOGGER.info("request input"+arrayList);
                }
            }


        aService.invoke(request);


         return null;
    }

}

package com.alibaba.smart.framework.engine.helper;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

public abstract class ExecutionInstanceHelper {


    public static ExecutionInstance findMatched(List<ExecutionInstance> executionInstanceList, String processDefinitionActivityId ){


        for (ExecutionInstance executionInstance : executionInstanceList) {
            if(executionInstance.getProcessDefinitionActivityId().equals(processDefinitionActivityId)){
                return executionInstance;
            }
        }

        return null;
    }
}

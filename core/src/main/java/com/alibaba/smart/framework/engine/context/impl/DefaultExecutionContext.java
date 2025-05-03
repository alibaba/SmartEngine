package com.alibaba.smart.framework.engine.context.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import lombok.Data;

/**
 *  Created by ettear on 16-4-19.
 */
@Data
public class DefaultExecutionContext implements ExecutionContext {

    private ExecutionContext parent;

    private ProcessInstance processInstance;

    private ExecutionInstance executionInstance;

    private BaseElement baseElement;

    private ActivityInstance activityInstance;

    private ProcessDefinition processDefinition;

    private ProcessEngineConfiguration processEngineConfiguration;

    private Map<String, Object> request;

    private Map<String, Object> response;

    private  Map<String, Object> innerExtra;

    private boolean needPause;

    @Deprecated
    private boolean nested;

    private String blockId; //目前仅用于包容网关 @2024.04.25

    @Override
    public String toString() {

        String processInstanceId = null;
        if(null != processInstance){
            processInstanceId=  processInstance.getInstanceId();
        }
        String executionId = null;
        String activityId = null;
        if(null != executionInstance){
            executionId=  executionInstance.getInstanceId();
            activityId = executionInstance.getProcessDefinitionActivityId();
        }
        return activityId+":" +processInstanceId+":"+ executionId +":" + blockId;
    }
}

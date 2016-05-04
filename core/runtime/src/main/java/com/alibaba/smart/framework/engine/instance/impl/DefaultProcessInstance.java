package com.alibaba.smart.framework.engine.instance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.instance.InstanceFact;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * Default Process Instance
 * Created by ettear on 16-4-12.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultProcessInstance extends AbstractLifeCycleInstance implements ProcessInstance {
    private String processUri;
    private String parentInstanceId;
    
    private String processDefinitionId;
    private String processDefinitionVersion;

    private String parentExecutionInstanceId;
    private String parentActivityInstanceId;

    private InstanceFact fact;
    /**
     * Running executions
     */
    private Map<String, ExecutionInstance> executions = new ConcurrentHashMap<>();
    
    /**
     * 需要顺序,并且不需要根据key来获取数据,所以是list数据结构
     */
    private List< ActivityInstance> activityInstances = new ArrayList<>();
    
    @Override
    public void addExecution(ExecutionInstance executionInstance) {
        this.executions.put(executionInstance.getInstanceId(),executionInstance);
    }

    @Override
    public void removeExecution(String executionInstanceId) {
        this.executions.remove(executionInstanceId);
    }

    @Override
    public void addActivityInstance(ActivityInstance activityInstance) {
        this.activityInstances.add(activityInstance) ;       
    }
}

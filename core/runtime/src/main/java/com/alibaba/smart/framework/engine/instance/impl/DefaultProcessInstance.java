package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.DatabaseMod;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.ProcessParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default Process Instance Created by ettear on 16-4-12.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultProcessInstance extends AbstractLifeCycleInstance implements ProcessInstance,DatabaseMod<DefaultProcessInstance,ProcessParam> {

    private static final long              serialVersionUID  = -201885591457164713L;


    private String                         processUri;
    private String                         processDefinitionId;
    private String                         processDefinitionVersion;

    private String                         parentInstanceId;
    private String                         parentExecutionInstanceId;
    private String                         parentActivityInstanceId;

    // private InstanceFact fact;
    /**
     * Running executions
     */
    private Map<String, ExecutionInstance> executions        = new ConcurrentHashMap<>();

    /**
     * 需要顺序,并且不需要根据key来获取数据,所以是list数据结构
     */
    private List<ActivityInstance>         activityInstances = new ArrayList<>();

    @Override
    public void addExecution(ExecutionInstance executionInstance) {
        this.executions.put(executionInstance.getInstanceId(), executionInstance);
    }

    @Override
    public void removeExecution(String executionInstanceId) {
        this.executions.remove(executionInstanceId);
    }

    @Override
    public void addActivityInstance(ActivityInstance activityInstance) {
        this.activityInstances.add(activityInstance);
    }

    @Override
    public String toDatabase() {

        StringBuilder data = new StringBuilder();
        for (ExecutionInstance executionInstance:this.getExecutions().values()) {

            if (executionInstance.isAbort()) {
                data.append("abort");
            }else if (executionInstance.isEnd()) {
                data.append("end");
            }else {
                data.append(executionInstance.toDatabase());
            }

            data.append("^");
        }

        return data.toString();

    }

    @Override
    public DefaultProcessInstance getModle(ProcessParam param) {
        this.setInstanceId(param.getProcessId());
        return this;

    }


    public String toString() {
        return this.toDatabase();

    }

}

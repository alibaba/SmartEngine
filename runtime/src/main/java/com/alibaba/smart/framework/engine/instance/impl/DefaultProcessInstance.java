package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.ProcessParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Default Process Instance Created by ettear on 16-4-12.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultProcessInstance extends AbstractLifeCycleInstance implements ProcessInstance{

    private static final long serialVersionUID = -201885591457164713L;


    private String processUri;
    private String processDefinitionId;
    private String processDefinitionVersion;

    private Long parentInstanceId;
//    private Long parentExecutionInstanceId;
//    private Long parentActivityInstanceId;


    private List<ActivityInstance> activityInstances = new ArrayList<>();

    @Override
    public void addNewActivityInstance(ActivityInstance activityInstance) {
        this.activityInstances.add(activityInstance);
    }

    public List<ActivityInstance> getNewActivityInstances(){
        return activityInstances;
    }


}

package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Default Process Instance Created by ettear on 16-4-12.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class DefaultProcessInstance extends AbstractLifeCycleInstance implements ProcessInstance {

    private static final long serialVersionUID = -201885591457164713L;

    private String processDefinitionIdAndVersion;
    private String processDefinitionId;
    private String processDefinitionVersion;
    private String processDefinitionType;

    private String tenantId;

    private String startUserId;
    /**
     * 业务唯一标识
     */
    private String bizUniqueId;

    private String parentInstanceId;
    private String parentExecutionInstanceId;

    private boolean suspend;

    private String reason;

    private String tag;

    private String title;

    /**
     * 备注
     */
    private String comment;


    @Setter
    private InstanceStatus status = InstanceStatus.running;
    private List<ActivityInstance> activityInstances = new ArrayList<ActivityInstance>();

    @Override
    public boolean isSuspend() {
        return InstanceStatus.suspended == this.status;
    }

    @Override
    public void addActivityInstance(ActivityInstance activityInstance) {
        this.activityInstances.add(activityInstance);
    }

    @Override
    public List<ActivityInstance> getActivityInstances() {
        return activityInstances;
    }


    @Override
    public String getUniqueProcessDefinitionIdAndVersion(){
        if(StringUtil.isEmpty(getTenantId())){
            return processDefinitionIdAndVersion;
        } else {
            return String.format("%s:%s", processDefinitionIdAndVersion, getTenantId());
        }
    }
}

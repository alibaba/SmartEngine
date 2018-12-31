package com.alibaba.smart.framework.engine.persister.mongo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExecutionInstanceEntity extends BaseProcessEntity {


    private String processInstanceId;

    private String processDefinitionIdAndVersion;

    private String processDefinitionActivityId;

    private String activityInstanceId;

    private String incomeTransitionId;

    private String incomeActivityInstanceId;

    private boolean active;
}

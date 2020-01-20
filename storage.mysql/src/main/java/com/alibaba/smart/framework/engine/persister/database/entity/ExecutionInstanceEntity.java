package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExecutionInstanceEntity extends BaseProcessEntity {


    private Long processInstanceId;

    private String processDefinitionIdAndVersion;

    private String processDefinitionActivityId;

    private Long activityInstanceId;

    private String incomeTransitionId;

    private Long incomeActivityInstanceId;

    private boolean active;
}

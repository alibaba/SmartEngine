package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExecutionInstanceEntity extends BaseProcessEntity {


    private Long id;

    private Long processInstanceId;

    private String processDefinitionActivityId;

    private Long activityInstanceId;

    private boolean active;
}

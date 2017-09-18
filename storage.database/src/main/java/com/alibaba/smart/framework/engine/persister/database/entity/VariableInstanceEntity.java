package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VariableInstanceEntity extends BaseProcessEntity {

    private Long id;

    private String type;

    private Long processInstanceId;

    private Long executionInstanceId;

    private Long taskInstanceId;

    private String assigneeId;

    private Integer priority;

}

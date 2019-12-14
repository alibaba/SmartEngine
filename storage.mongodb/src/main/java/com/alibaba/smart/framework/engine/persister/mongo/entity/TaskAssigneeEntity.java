package com.alibaba.smart.framework.engine.persister.mongo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskAssigneeEntity extends BaseProcessEntity {

    private String processInstanceId;

    private String taskInstanceId;

    private String assigneeId;

    private String assigneeType;

    //private String processDefinitionType;

}

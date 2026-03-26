package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserTaskIndexEntity extends BaseProcessEntity {

    private String assigneeId;
    private String assigneeType;
    private Long taskInstanceId;
    private Long processInstanceId;
    private String processDefinitionType;
    private String domainCode;
    private String extra;
    private String taskStatus;
    private java.util.Date taskGmtModified;
    private String title;
    private Integer priority;
}

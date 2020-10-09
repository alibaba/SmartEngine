package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessInstanceEntity extends BaseProcessEntity {

    private String processDefinitionIdAndVersion;

    private String startUserId;

    private Long parentProcessInstanceId;

    private Long parentExecutionInstanceId;

    private String status;

    private String processDefinitionType;

    private String bizUniqueId;

    private String reason;

    private String title;

    private String tag;

    private String comment;

}

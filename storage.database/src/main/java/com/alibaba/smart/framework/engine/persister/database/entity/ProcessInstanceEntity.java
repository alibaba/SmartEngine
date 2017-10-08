package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcessInstanceEntity extends BaseProcessEntity {

    private String processDefinitionIdAndVersion;

    private  String startUserId;

    private Long parentProcessInstanceId;

    private String status;

    private String processDefinitionType;


}

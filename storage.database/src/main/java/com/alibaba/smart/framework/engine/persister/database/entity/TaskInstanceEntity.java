package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskInstanceEntity extends BaseProcessEntity {


    private Long processInstanceId;

    private String processDefinitionIdAndVersion;

    private Long executionInstanceId;

    private Long activityInstanceId;

    private  String processDefinitionType;

    private String  processDefinitionActivityId;

    private String claimUserId;

    private Integer priority;

    private String status;

    private  String tag;

    private Date claimTime;

    private Date completeTime;
}

package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskInstanceEntity extends BaseProcessEntity {

    private Long id;

    private Long processInstanceId;

    private Long executionInstanceId;

    private Long activityInstanceId;

    private String claimUserId;

    private Integer priority;

    private String status;

    private Date claimTime;

    private Date completeTime;
}

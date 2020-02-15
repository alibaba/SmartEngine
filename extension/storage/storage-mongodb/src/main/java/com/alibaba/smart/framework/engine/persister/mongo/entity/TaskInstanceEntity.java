package com.alibaba.smart.framework.engine.persister.mongo.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskInstanceEntity extends BaseProcessEntity {


    private String processInstanceId;

    private String processDefinitionIdAndVersion;

    private String executionInstanceId;

    private String activityInstanceId;

    private  String processDefinitionType;

    private String  processDefinitionActivityId;

    private String claimUserId;

    private Integer priority;

    private String status;

    private  String tag;

    private Date claimTime;

    private Date completeTime;

    /**
     * 备注(处理意见)
     */
    private String comment;

    /**
     * 扩展字段(完全由使用方控制)
     */
    private String extension;

    /**
     * 任务标题
     */
    private String title;

    private List<TaskAssigneeEntity> assigneeList;
}

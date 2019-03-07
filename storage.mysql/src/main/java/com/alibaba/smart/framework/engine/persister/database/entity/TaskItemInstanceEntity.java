package com.alibaba.smart.framework.engine.persister.database.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskItemInstanceEntity extends BaseProcessEntity {


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

    /**
     * 主任务实例ID
     */
    private Long taskInstanceId;

    /**
     * 主业务id
     */
    private String bizId;

    /**
     * 子业务id
     */
    private String subBizId;

    /**
     * 子业务id列表
     */
    private List<String> subBizIdList;

    /**
     * 源状态，
     */
    private String fromStatus;


}

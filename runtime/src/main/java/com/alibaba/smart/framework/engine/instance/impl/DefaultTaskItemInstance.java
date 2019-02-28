package com.alibaba.smart.framework.engine.instance.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认任务实例 Created by ettear on 16-4-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTaskItemInstance extends AbstractLifeCycleInstance implements TaskItemInstance {

    private static final long serialVersionUID = -3920292154786127202L;
    private String processDefinitionIdAndVersion;

    private String processDefinitionActivityId;

    private  String processDefinitionType;

    private String activityInstanceId;
    private String processInstanceId;
    private String executionInstanceId;

    /**
     *任务认领人，也即是任务处理者。
     */
    private String claimUserId;

    private String tag;



    private List<TaskAssigneeInstance> taskAssigneeInstanceList;

    private Integer priority;
    private Date claimTime;

    private String status;

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
     * 主任务id
     */
    Long taskInstanceId;

    /**
     * 子业务id
     */
    String subBizId;

    /**
     * 主业务id
     */
    String bizId;
}

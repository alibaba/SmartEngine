package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 默认任务实例 Created by ettear on 16-4-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTaskInstance extends AbstractLifeCycleInstance implements TaskInstance {

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

}

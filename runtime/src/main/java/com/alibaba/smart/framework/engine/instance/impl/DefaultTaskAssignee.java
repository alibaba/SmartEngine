package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.TaskAssignee;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 默认任务处理者 Created by 帝奇 on 17.01.02.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTaskAssignee extends AbstractLifeCycleInstance implements TaskAssignee {

    private static final long serialVersionUID = -3920292154786127202L;
    private String processDefinitionIdAndVersion;

    private String activityId;
    private Long processInstanceId;
    //private Long executionInstanceId;
    //private Long activityInstanceId;
    //
    //private String title;
    private String assigneeId;
    private Integer priority;
    private Date claimTime;
    private Date endTime;

}

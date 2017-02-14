package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 默认任务实例 Created by ettear on 16-4-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTaskInstance extends AbstractLifeCycleInstance implements TaskInstance {

    private static final long serialVersionUID = -3920292154786127202L;
    private String processDefinitionIdAndVersion;

    private String activityId;
    private Long processInstanceId;
    private Long executionInstanceId;
    private Long activityInstanceId;

    private String assigneeId;
    private Integer priority;
    private Date claimTime;
    private Date endTime;

}

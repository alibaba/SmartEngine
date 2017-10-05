package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DefaultExecutionInstance Created by ettear on 16-4-19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance {

    private static final long serialVersionUID = 2323809298485587299L;
    private String processDefinitionIdAndVersion;
    private Long processInstanceId;
    private Long activityInstanceId;

    private String processDefinitionActivityId;
    private boolean active;

    private TaskInstance taskInstance;






}

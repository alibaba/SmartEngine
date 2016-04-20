package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.TaskInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认任务实例
 * Created by ettear on 16-4-20.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultTaskInstance extends AbstractInstance implements TaskInstance {
    private String name;
    private String processInstanceId;
    private String executionInstanceId;
    private String activityInstanceId;
}

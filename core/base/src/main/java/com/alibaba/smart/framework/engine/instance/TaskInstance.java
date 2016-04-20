package com.alibaba.smart.framework.engine.instance;

/**
 * 任务实例
 * Created by ettear on 16-4-13.
 */
public interface TaskInstance extends Instance {
    String getName();
    String getProcessInstanceId();
    String getExecutionInstanceId();
    String getActivityInstanceId();
}

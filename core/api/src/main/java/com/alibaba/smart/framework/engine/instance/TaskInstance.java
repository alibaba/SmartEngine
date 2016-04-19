package com.alibaba.smart.framework.engine.instance;

/**
 * Created by ettear on 16-4-13.
 */
public interface TaskInstance {
    String getId();
    String getExecutionInstanceId();
    String getActivityInstanceId();
    String getStatus();
}

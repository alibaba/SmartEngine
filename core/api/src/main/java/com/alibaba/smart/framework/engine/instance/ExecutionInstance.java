package com.alibaba.smart.framework.engine.instance;

/**
 * Created by ettear on 16-4-18.
 */
public interface ExecutionInstance {
    String getId();
    String getProcessInstanceId();
    String getStatus();
    ActivityInstance getActivity();
}

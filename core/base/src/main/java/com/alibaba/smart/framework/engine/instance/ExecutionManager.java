package com.alibaba.smart.framework.engine.instance;

/**
 * Created by ettear on 16-4-18.
 */
public interface ExecutionManager {

    ExecutionInstance create(String processInstanceId, ExecutionInstance execution);

    ExecutionInstance create(String processInstanceId, String parentId,ExecutionInstance execution);
}

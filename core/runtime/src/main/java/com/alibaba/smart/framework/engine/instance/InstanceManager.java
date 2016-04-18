package com.alibaba.smart.framework.engine.instance;

import com.alibaba.smart.framework.engine.invocation.Event;

/**
 * Created by ettear on 16-4-12.
 */
public interface InstanceManager {
    ProcessInstance start(String processId,String version,Event startEvent);

    void stop(String instanceId);

    ProcessInstance load(String instanceId);

}

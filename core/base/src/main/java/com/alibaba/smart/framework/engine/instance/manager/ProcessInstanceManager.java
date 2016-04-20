package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.context.EventContext;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProcessInstanceManager {

    ProcessInstance create(ProcessInstance processInstance);

    void abort(String instanceId);

    ProcessInstance load(String instanceId);

}

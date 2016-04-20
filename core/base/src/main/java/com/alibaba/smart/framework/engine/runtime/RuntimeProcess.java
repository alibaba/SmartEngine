package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.context.InstanceContext;

import java.util.Map;

/**
 * RuntimeProcess
 * Created by ettear on 16-4-12.
 */
public interface RuntimeProcess extends RuntimeActivity<Process> {

    Map<String, RuntimeActivity> getActivities();
}

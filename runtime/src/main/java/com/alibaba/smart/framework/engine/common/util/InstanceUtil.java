package com.alibaba.smart.framework.engine.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2018 August  16:46.
 */
public abstract class InstanceUtil {
    private static final ReentrantLock LOCK = new ReentrantLock();

    public  static   List<ExecutionInstance> findActiveExecution (ProcessInstance processInstance){
        List<ActivityInstance> activityInstances;
        LOCK.lock();
        try {
            if (null == processInstance.getActivityInstances()) {
                return null;
            }
            activityInstances = new ArrayList<ActivityInstance>(processInstance.getActivityInstances());
        } finally {
            LOCK.unlock();
        }

        //TUNE 扩容
        List<ExecutionInstance> executionInstances = new ArrayList<ExecutionInstance>(activityInstances.size());
        for (ActivityInstance activityInstance : activityInstances) {
            List<ExecutionInstance> eiInactivity = activityInstance.getExecutionInstanceList();
            if (eiInactivity != null) {
                for (ExecutionInstance executionInstance : eiInactivity) {
                    if (null != executionInstance && executionInstance.isActive()) {
                        executionInstances.add(executionInstance);
                    }
                }
            }
        }

        return executionInstances;
    }
}
package com.alibaba.smart.framework.engine.common.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2018 August  16:46.
 */
public abstract class InstanceUtil {

    public  static   List<ExecutionInstance> findActiveExecution (ProcessInstance processInstance){
        List<ActivityInstance> activityInstances =  processInstance.getActivityInstances();
        if(null==activityInstances){
            return null;
        }

        List<ExecutionInstance> matchedExecutionInstanceList = new ArrayList<ExecutionInstance>(activityInstances.size());
        for (ActivityInstance activityInstance : activityInstances) {
            List<ExecutionInstance> executionInstancesOfAI = activityInstance.getExecutionInstanceList();
            if (CollectionUtil.isNotEmpty(executionInstancesOfAI)) {
                for (ExecutionInstance executionInstance : executionInstancesOfAI) {
                    if (null != executionInstance && executionInstance.isActive()) {
                        matchedExecutionInstanceList.add(executionInstance);
                    }
                }
            }
        }

        return matchedExecutionInstanceList;
    }
}
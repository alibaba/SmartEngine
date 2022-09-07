package com.alibaba.smart.framework.engine.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.util.ObjectUtil;

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


    public  static void enrich(Map<String, Object> request, TaskInstance taskInstance) {

        if(null!= request){
            String comment = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT));
            taskInstance.setComment(comment);

            String extension = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_EXTENSION));
            taskInstance.setExtension(extension);

            Integer priority = ObjectUtil.obj2Integer(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_PRIORITY));
            taskInstance.setPriority(priority);


            String title = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_TITLE));
            taskInstance.setTitle(title);
        }

    }
}
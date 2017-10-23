package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  10:35.
 */
public class MarkDoneUtil {

    public   static  ExecutionInstance markDoneExecutionInstance(ExecutionInstance executionInstance, ExecutionInstanceStorage executionInstanceStorage) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);
        executionInstanceStorage.update(executionInstance);
        return  executionInstance;
    }

    public static TaskInstance markDoneTaskInstance(TaskInstance taskInstance, String targetStatus,Map<String, Object> variables,
                                              TaskInstanceStorage taskInstanceStorage) {
        Date currentDate = DateUtil.getCurrentDate();
        taskInstance.setCompleteTime(currentDate);

        if(null == taskInstance.getClaimTime()){
            taskInstance.setClaimTime(currentDate);
        }

        taskInstance.setStatus(targetStatus);

        if(null != variables){
            String tag = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            taskInstance.setTag(tag);

            String claimUserId = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID);
            taskInstance.setClaimUserId(claimUserId);
        }

        taskInstanceStorage.update(taskInstance);
        return taskInstance;
    }

}

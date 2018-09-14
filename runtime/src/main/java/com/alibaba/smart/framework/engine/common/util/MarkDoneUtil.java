package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  10:35.
 */
public class MarkDoneUtil {

    public static ExecutionInstance markDoneExecutionInstance(ExecutionInstance executionInstance,
                                                              ExecutionInstanceStorage executionInstanceStorage) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);
        executionInstance.setStatus(InstanceStatus.completed);

        //产生了 DB 写，是否需要干掉。
        executionInstanceStorage.update(executionInstance);
        return executionInstance;
    }

    public static ExecutionInstance markDoneExecutionInstance(ExecutionInstance executionInstance) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);
        executionInstance.setStatus(InstanceStatus.completed);


        return executionInstance;
    }

    public static TaskInstance markDoneTaskInstance(TaskInstance taskInstance, String targetStatus,String sourceStatus,
                                                    Map<String, Object> variables,
                                                    TaskInstanceStorage taskInstanceStorage) {
        Date currentDate = DateUtil.getCurrentDate();
        taskInstance.setCompleteTime(currentDate);

        if (null == taskInstance.getClaimTime()) {
            taskInstance.setClaimTime(currentDate);
        }

        taskInstance.setStatus(targetStatus);

        if (null != variables) {
            String tag = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            taskInstance.setTag(tag);

            String claimUserId = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID);
            taskInstance.setClaimUserId(claimUserId);
            String comment = variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT) == null?null:String.valueOf(variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT));
            taskInstance.setClaimUserId(claimUserId);
            taskInstance.setComment(comment);

        }

        int updateCount = taskInstanceStorage.updateFromStatus(taskInstance, sourceStatus);
        if (updateCount != 1) {
            throw new ConcurrentException(String
                .format("update_task_status_fail task_id=%s expect_from_[%s]_to_[%s]", taskInstance.getInstanceId(), sourceStatus,
                    taskInstance.getStatus()));
        }
        return taskInstance;
    }

}

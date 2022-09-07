package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.util.ObjectUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  10:35.
 */
public class MarkDoneUtil {

    public static ExecutionInstance markDoneExecutionInstance(ExecutionInstance executionInstance,
                                                              ExecutionInstanceStorage executionInstanceStorage,
                                                              ProcessEngineConfiguration processEngineConfiguration) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);

        executionInstanceStorage.update(executionInstance, processEngineConfiguration);
        return executionInstance;
    }


    public static TaskInstance markDoneTaskInstance(TaskInstance taskInstance, String targetStatus, String sourceStatus,
                                                    Map<String, Object> variables,
                                                    TaskInstanceStorage taskInstanceStorage,
                                                    ProcessEngineConfiguration processEngineConfiguration) {
        Date currentDate = DateUtil.getCurrentDate();
        taskInstance.setCompleteTime(currentDate);

        if (null == taskInstance.getClaimTime()) {
            taskInstance.setClaimTime(currentDate);
        }

        taskInstance.setStatus(targetStatus);

        if (null != variables) {
            String tag = ObjectUtil.obj2Str(variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG));
            taskInstance.setTag(tag);

            String claimUserId = ObjectUtil.obj2Str(variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID));
            taskInstance.setClaimUserId(claimUserId);
            Object o = variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT);
            String comment =  o == null?null:String.valueOf(
                o);
            taskInstance.setClaimUserId(claimUserId);
            taskInstance.setComment(comment);

        }

        // 需要注意，针对 mongodb 模式，该方法会在内部实现，删除人员和任务的冗余存储关系。
        int updateCount = taskInstanceStorage.updateFromStatus(taskInstance, sourceStatus, processEngineConfiguration);
        if (updateCount != 1) {
            throw new ConcurrentException(String
                .format("update_task_status_fail task_id=%s expect_from_[%s]_to_[%s]", taskInstance.getInstanceId(), sourceStatus,
                    taskInstance.getStatus()));
        }
        return taskInstance;
    }

}

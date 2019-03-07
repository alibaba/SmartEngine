package com.alibaba.smart.framework.engine.common.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;

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
        executionInstance.setStatus(InstanceStatus.completed);

        //TODO 产生了 DB 写，是否需要干掉。
        executionInstanceStorage.update(executionInstance, processEngineConfiguration);
        return executionInstance;
    }

    public static ExecutionInstance markDoneExecutionInstance(ExecutionInstance executionInstance) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteTime(completeDate);
        executionInstance.setActive(false);
        executionInstance.setStatus(InstanceStatus.completed);


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
            String tag = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG);
            taskInstance.setTag(tag);

            String claimUserId = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID);
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

    public static TaskItemInstance markDoneTaskItemInstance(TaskItemInstance taskItemInstance, String targetStatus, String sourceStatus,
                                                        Map<String, Object> variables,
                                                        TaskItemInstanceStorage taskItemInstanceStorage,
                                                        ProcessEngineConfiguration processEngineConfiguration) {
        Date currentDate = DateUtil.getCurrentDate();
        taskItemInstance.setCompleteTime(currentDate);
        if (null == taskItemInstance.getClaimTime()) {
            taskItemInstance.setClaimTime(currentDate);
        }
        taskItemInstance.setStatus(targetStatus);

        if (null != variables) {
            String tag = (String)variables.get(RequestMapSpecialKeyConstant.TASK_ITEM_INSTANCE_TAG);
            taskItemInstance.setTag(tag);
            String claimUserId = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID);
            taskItemInstance.setClaimUserId(claimUserId);
            Object o = variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT);
            String comment =  o == null?null:String.valueOf(o);
            taskItemInstance.setClaimUserId(claimUserId);
            taskItemInstance.setComment(comment);
        }
        // 需要注意，针对 mongodb 模式，该方法会在内部实现，删除人员和任务的冗余存储关系。
        int updateCount = taskItemInstanceStorage.updateFromStatus(taskItemInstance, sourceStatus, processEngineConfiguration);
        if (updateCount != 1) {
            throw new ConcurrentException(String
                .format("update_task_status_fail task_id=%s expect_from_[%s]_to_[%s]", taskItemInstance.getInstanceId(), sourceStatus,
                    taskItemInstance.getStatus()));
        }
        return taskItemInstance;
    }

    public static List<TaskItemInstance> markDoneTaskItemInstance(List<TaskItemInstance> taskItemInstanceList, String targetStatus, String sourceStatus,
                                                            Map<String, Object> variables,
                                                            TaskItemInstanceStorage taskItemInstanceStorage,
                                                            ProcessEngineConfiguration processEngineConfiguration) {
        Date currentDate = DateUtil.getCurrentDate();
        for(TaskItemInstance taskItemInstance:taskItemInstanceList){
            taskItemInstance.setCompleteTime(currentDate);
            if (null == taskItemInstance.getClaimTime()) {
                taskItemInstance.setClaimTime(currentDate);
            }
            taskItemInstance.setStatus(targetStatus);

            if (null != variables) {
                String tag = (String)variables.get(RequestMapSpecialKeyConstant.TASK_ITEM_INSTANCE_TAG);
                taskItemInstance.setTag(tag);
                String claimUserId = (String)variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID);
                taskItemInstance.setClaimUserId(claimUserId);
                Object o = variables.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT);
                String comment =  o == null?null:String.valueOf(o);
                taskItemInstance.setComment(comment);
            }

        }
        int updateCount = taskItemInstanceStorage.updateStatusBatch(taskItemInstanceList, sourceStatus, processEngineConfiguration);
        if (updateCount <taskItemInstanceList.size()) {
            throw new ConcurrentException(String
                    .format("update_task_status_batch_fail task_id=%s expect_from_[%s]_to_[%s]", taskItemInstanceList.get(0).getInstanceId(), sourceStatus,
                            taskItemInstanceList.get(0).getStatus()));
        }

        return taskItemInstanceList;
    }

}

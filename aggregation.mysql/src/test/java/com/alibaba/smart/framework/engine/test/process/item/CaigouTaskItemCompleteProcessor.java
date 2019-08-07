package com.alibaba.smart.framework.engine.test.process.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.configuration.TaskItemCompleteProcessor;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.UserTask;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskItemQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.process.FullMultiInstanceTest;


public class CaigouTaskItemCompleteProcessor implements TaskItemCompleteProcessor {

    @Override
    public void postProcessBeforeTaskItemComplete(String processInstanceId, String activityInstanceId,
                                                  String taskInstanceId, List<String> taskItemInstanceId,
                                                  Map<String, Object> variables,
                                                  Activity activity, SmartEngine smartEngine) {
        if (activity instanceof UserTask) {
            UserTask userTask = (UserTask)activity;
            Map<String, String> properties = userTask.getProperties();
            String approvalType = properties.get("approvalType");
            if ("anyone".equals(approvalType)) {
                TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
                taskInstanceQueryParam.setProcessInstanceIdList(Collections.singletonList(processInstanceId));
                taskInstanceQueryParam.setActivityInstanceId(activityInstanceId);
                TaskQueryService taskQueryService = smartEngine.getTaskQueryService();
                List<TaskInstance> taskInstanceList = taskQueryService.findList(taskInstanceQueryParam);
                if (taskInstanceList == null || taskInstanceList.size() <= 0) {
                    return;
                }

                boolean isComplete = true;

                for(TaskInstance taskInstance : taskInstanceList){
                    if(taskInstance.getInstanceId().equals(taskInstanceId) && TaskInstanceConstant.PENDING.equals(taskInstance.getStatus())){
                        isComplete = false;
                        break;
                    }
                }

                if (isComplete) {
                    throw new RuntimeException("该单据已在审批中");
                }
                List<TaskInstance> pendingTaskList = new ArrayList<TaskInstance>();
                for(TaskInstance taskInstance : taskInstanceList){
                    if(taskInstance.getStatus().equals(TaskInstanceConstant.PENDING)){
                        pendingTaskList.add(taskInstance);
                    }
                }
                if (pendingTaskList.size() > 0) {
                    TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
                    for (TaskInstance taskInstance : pendingTaskList) {
                        if (taskInstance.getInstanceId().equals(taskInstanceId)) {
                            continue;
                        }
                        taskCommandService.cancel(taskInstance.getInstanceId(), null);
                    }
                }
            }
        }
    }

    @Override
    public void postProcessAfterTaskItemComplete(String processInstanceId, String activityInstanceId,
                                                 String taskInstanceId, List<String> taskItemInstanceId,
                                                 Map<String, Object> variables,
                                                 Activity activity, SmartEngine smartEngine) {
    }

    @Override
    public Map<String, String> canCompleteCurrentMainTask(String processInstanceId, String taskInstanceId,
                                                          Activity activity, SmartEngine smartEngine) {
        TaskItemQueryService taskItemQueryService = smartEngine.getTaskItemQueryService();
        TaskItemInstanceQueryParam taskItemInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskItemInstanceQueryParam.setTaskInstanceId(taskInstanceId);
        taskItemInstanceQueryParam.setProcessInstanceIdList(Collections.singletonList(processInstanceId));
        List<TaskItemInstance> taskItemList = taskItemQueryService.findTaskItemList(taskItemInstanceQueryParam);
        if (CollectionUtil.isEmpty(taskItemList)) {
            return null;
        }
        int allCompletedCount = 0;
        int allAgreeCount = 0;
        int allDisagreeCount = 0;
        for(TaskItemInstance itemInstance : taskItemList){
            if(TaskInstanceConstant.COMPLETED.equals(itemInstance.getStatus())){
                allCompletedCount++;
            }
            if(FullMultiInstanceTest.AGREE.equals(itemInstance.getTag())){
                allAgreeCount++;
            }
            if(FullMultiInstanceTest.DISAGREE.equals(itemInstance.getStatus())){
                allDisagreeCount++;
            }
        }
        if (allCompletedCount == taskItemList.size()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("canComplete", "true");
            boolean allAgree = allAgreeCount == taskItemList.size();
            boolean allDisagree = allDisagreeCount == taskItemList.size();;
            if (allAgree) {
                map.put("tag", FullMultiInstanceTest.AGREE);
            } else if (allDisagree) {
                map.put("tag", FullMultiInstanceTest.DISAGREE);
            } else {
                map.put("tag", FullMultiInstanceTest.PART_AGREE);
            }
            return map;
        }
        return null;
    }

    @Override
    public List<String> getPassedSubBizIdByActivityInstanceId(String activityInstanceId, SmartEngine smartEngine) {
        TaskItemQueryService taskItemQueryService = smartEngine.getTaskItemQueryService();
        return taskItemQueryService.getPassSubBizIdByActivityId(activityInstanceId, FullMultiInstanceTest.AGREE);
    }
}

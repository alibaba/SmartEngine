package com.alibaba.smart.framework.engine.util;

import java.util.Date;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * Created by 高海军 帝奇 74394 on  2020-12-16 18:25.
 */
public abstract class RequestHelper {

    public static void buildTaskInstanceFromRequest(Map<String, Object> request, TaskInstance taskInstance) {
        String processDefinitionType = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE));
        taskInstance.setProcessDefinitionType(processDefinitionType);

        Date startTime = ObjUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.TASK_START_TIME));
        taskInstance.setStartTime(startTime);

        Date completeTime = ObjUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.TASK_COMPLETE_TIME));
        taskInstance.setCompleteTime(completeTime);

        String comment = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_COMMENT));
        taskInstance.setComment(comment);

        String extension = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_EXTENSION));
        taskInstance.setExtension(extension);

        Integer priority = ObjUtil.obj2Integer(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_PRIORITY));
        taskInstance.setPriority(priority);

        String tag = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG));
        taskInstance.setTag(tag);

        String title = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TASK_TITLE));
        taskInstance.setTitle(title);

        String claimUserId = ObjUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.CLAIM_USER_ID));
        taskInstance.setClaimUserId(claimUserId);

        Date claimTime = ObjUtil.obj2Date(request.get(RequestMapSpecialKeyConstant.CLAIM_USER_TIME));
        taskInstance.setClaimTime(claimTime);
    }
}
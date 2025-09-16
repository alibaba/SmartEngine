package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

import java.util.List;
import java.util.Map;

/** 任务委托信息 */
public interface TaskAssigneeQueryService {

    List<TaskAssigneeInstance> findList(String taskInstanceId);

    List<TaskAssigneeInstance> findList(String taskInstanceId, String tenantId);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(
            List<String> taskInstanceIdList);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(
            List<String> taskInstanceIdList, String tenantId);
}

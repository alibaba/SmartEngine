package com.alibaba.smart.framework.engine.service.query;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

/**
 * <p>任务委托信息</p>
 */
public interface TaskAssigneeQueryService {

    List<TaskAssigneeInstance> findList(String taskInstanceId);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList);

    List<TaskAssigneeInstance> findAll(String processInstanceId);

}

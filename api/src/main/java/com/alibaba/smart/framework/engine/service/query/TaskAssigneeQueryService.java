package com.alibaba.smart.framework.engine.service.query;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

public interface TaskAssigneeQueryService {

    List<TaskAssigneeInstance> findList(Long taskInstanceId);

    Map<Long, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<Long> taskInstanceIdList);

}

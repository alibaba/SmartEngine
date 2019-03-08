package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

public interface TaskItemQueryService {
    Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam);

    List<TaskItemInstance> findTaskItemList(TaskItemInstanceQueryParam taskItemInstanceQueryParam);
}

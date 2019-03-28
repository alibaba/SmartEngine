package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

public interface TaskItemQueryService {

    Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam);

    List<TaskItemInstance> findTaskItemList(TaskItemInstanceQueryParam taskItemInstanceQueryParam);

    /**
     * 获取交集结果
     * @param activityInstanceId 活动实例id
     * @return
     */
    boolean taskItemIntersectionResult(String activityInstanceId, String passTag);

    /**
     * 获取交集通过的，子业务id
     * @param activityInstanceId 活动实例id
     * @return
     */
    List<String> getPassSubBizIdByActivityId(String activityInstanceId, String passTag);


    List<String> getPassPassSubBizId(String processInstanceId, String passTag);
}

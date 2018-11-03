package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * 用户任务查询服务。
 *
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface TaskQueryService {

    /**
     * 待办任务列表
     */
    List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);

    Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);

    /**
     * 不分页查询，大数据量下慎用。
     *
     * @param processInstanceId
     * @return
     */
    List<TaskInstance> findAllPendingTaskList(String processInstanceId);






    TaskInstance findOne(String taskInstanceId );


    /**
     * 扩展方法，可用于典型的审批场景等等，取决于tag的值是什么。tag 任意非null值，可以为 appproved,rejected 等等。
     *
     */
    List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam);

    Long count(TaskInstanceQueryParam taskInstanceQueryParam);

}

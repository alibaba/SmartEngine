package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;

import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface TaskInstanceQueryService {

    /**
     * 待办任务列表
     */
    List<TaskInstance> findPendingTask(Long processInstanceId, Long userId, PaginateRequest paginateRequest);

    /**
     * 不分页查询，大数据量下慎用。
     *
     * @param processInstanceId
     * @return
     */
    List<TaskInstance> findAllPendingTasks(Long processInstanceId);

    /**
     * 不分页查询，大数据量下慎用。
     * @param processInstanceId
     * @param userId
     * @return
     */
    List<TaskInstance> findAllPendingTasks(Long processInstanceId,Long userId);

    /**
     * 扩展方法，可用于典型的审批场景等等，取决于tag的值是什么。
     *
     * @param processInstanceId
     * @param tag 任意非null值，可以为 appproved,rejected 等等。
     */
    List<TaskInstance> findTaskByTag(Long processInstanceId,String tag,PaginateRequest paginateRequest);

}

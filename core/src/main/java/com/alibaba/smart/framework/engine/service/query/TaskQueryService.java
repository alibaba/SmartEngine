package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.CompletedTaskQueryParam;

/**
 * 用户任务查询服务。
 *
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface TaskQueryService {

    /**
     * 待办任务列表
     *
     * @deprecated Use {@code smartEngine.createTaskQuery().taskCandidateOrGroup(userId, groupIds).taskStatus("pending").list()} instead
     */
    @Deprecated
    List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().taskCandidateOrGroup(userId, groupIds).taskStatus("pending").count()} instead
     */
    @Deprecated
    Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().taskCandidateOrGroup(userId, groupIds).taskStatus(s).list()} instead
     */
    @Deprecated
    List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().taskCandidateOrGroup(userId, groupIds).taskStatus(s).count()} instead
     */
    @Deprecated
    Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param);

    /**
     * 不分页查询，大数据量下慎用。
     *
     * @param processInstanceId
     * @return
     * @deprecated Use {@code smartEngine.createTaskQuery().processInstanceId(id).taskStatus("pending").list()} instead
     */
    @Deprecated
    List<TaskInstance> findAllPendingTaskList(String processInstanceId);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().processInstanceId(id).taskStatus("pending").tenantId(t).list()} instead
     */
    @Deprecated
    List<TaskInstance> findAllPendingTaskList(String processInstanceId,String tenantId);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().taskInstanceId(id).singleResult()} instead
     */
    @Deprecated
    TaskInstance findOne(String taskInstanceId);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery().taskInstanceId(id).tenantId(t).singleResult()} instead
     */
    @Deprecated
    TaskInstance findOne(String taskInstanceId,String tenantId);

    /**
     * 扩展方法，可用于典型的审批场景等等，取决于tag的值是什么。tag 任意非null值，可以为 appproved,rejected 等等。
     *
     * @deprecated Use {@code smartEngine.createTaskQuery()} fluent API instead
     */
    @Deprecated
    List<TaskInstance> findList(TaskInstanceQueryParam taskInstanceQueryParam);

    /**
     * @deprecated Use {@code smartEngine.createTaskQuery()...count()} instead
     */
    @Deprecated
    Long count(TaskInstanceQueryParam taskInstanceQueryParam);

    /**
     * 查询已办任务列表 - 基于现有findList方法扩展
     *
     * @param param 已办任务查询参数
     * @return 已办任务列表
     * @deprecated Use {@code smartEngine.createTaskQuery().taskAssignee(u).processDefinitionTypeIn(types).completeTimeAfter(s).completeTimeBefore(e).taskStatus("completed").list()} instead
     */
    @Deprecated
    List<TaskInstance> findCompletedTaskList(CompletedTaskQueryParam param);

    /**
     * 统计已办任务数量
     *
     * @param param 已办任务查询参数
     * @return 已办任务数量
     * @deprecated Use {@code smartEngine.createTaskQuery().taskAssignee(u).processDefinitionTypeIn(types).completeTimeAfter(s).completeTimeBefore(e).taskStatus("completed").count()} instead
     */
    @Deprecated
    Long countCompletedTaskList(CompletedTaskQueryParam param);

}

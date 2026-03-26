package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.CompletedProcessQueryParam;

/**
 * 查询流程实例。
 *
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ProcessQueryService {

    /**
     * @deprecated Use {@code smartEngine.createProcessQuery().processInstanceId(id).singleResult()} instead
     */
    @Deprecated
    ProcessInstance findById(String processInstanceId);

    /**
     * @deprecated Use {@code smartEngine.createProcessQuery().processInstanceId(id).tenantId(t).singleResult()} instead
     */
    @Deprecated
    ProcessInstance findById(String processInstanceId,String tenantId);

    /**
     * @deprecated Use {@code smartEngine.createProcessQuery()} fluent API instead
     */
    @Deprecated
    List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam);

    /**
     * @deprecated Use {@code smartEngine.createProcessQuery()...count()} instead
     */
    @Deprecated
    Long count(ProcessInstanceQueryParam processInstanceQueryParam);

    /**
     * 查询办结流程列表 - 基于现有findList方法扩展
     *
     * @param param 办结流程查询参数
     * @return 办结流程列表
     * @deprecated Use {@code smartEngine.createProcessQuery().involvedUser(u).processDefinitionTypeIn(types).completedAfter(s).completedBefore(e).processStatus("completed").list()} instead
     */
    @Deprecated
    List<ProcessInstance> findCompletedProcessList(CompletedProcessQueryParam param);

    /**
     * 统计办结流程数量
     *
     * @param param 办结流程查询参数
     * @return 办结流程数量
     * @deprecated Use {@code smartEngine.createProcessQuery().involvedUser(u).processDefinitionTypeIn(types).completedAfter(s).completedBefore(e).processStatus("completed").count()} instead
     */
    @Deprecated
    Long countCompletedProcessList(CompletedProcessQueryParam param);

}

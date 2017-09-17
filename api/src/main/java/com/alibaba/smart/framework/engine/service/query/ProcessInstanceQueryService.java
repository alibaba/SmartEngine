package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ProcessInstanceQueryService {

    ProcessInstance findOne(Long processInstanceId);

    List<ProcessInstance> findStartedProcessInstance(Long startProcessUserId, PaginateRequest paginateRequest);

    List<ProcessInstance> findStartedProcessInstanceBySatus(Long startProcessUserId,String processStatus, PaginateRequest paginateRequest);


}

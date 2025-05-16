package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

/**
 * 查询流程实例。
 *
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ProcessQueryService {

    ProcessInstance findById(String processInstanceId);

    ProcessInstance findById(String processInstanceId,String tenantId);

    List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam);

    Long count(ProcessInstanceQueryParam processInstanceQueryParam);

}

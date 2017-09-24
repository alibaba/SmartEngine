package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;
import com.alibaba.smart.framework.engine.service.param.ProcessInstanceParam;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ProcessInstanceQueryService {

    ProcessInstance findOne(Long processInstanceId);

    List<ProcessInstance> queryProcessInstanceList(ProcessInstanceParam processInstanceParam);




}

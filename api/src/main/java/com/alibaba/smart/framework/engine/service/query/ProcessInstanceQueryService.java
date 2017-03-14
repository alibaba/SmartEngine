package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ProcessInstanceQueryService {

    ProcessInstance findOne(Long processInstanceId);

}

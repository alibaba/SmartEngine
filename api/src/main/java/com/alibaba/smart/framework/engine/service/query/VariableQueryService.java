package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:45.
 */
public interface VariableQueryService {

     List<VariableInstance> findProcessInstanceVariableList(Long processInstanceId);

     List<VariableInstance> findList(Long processInstanceId, Long executionInstanceId);
}

package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:45.
 */
public interface VariableQueryService {

    /**
     *获取指定流程实例关联的变量数据.
     *
     */
     List<VariableInstance> findProcessInstanceVariableList(String processInstanceId);

    /**
     *获取指定流程实例以及对应执行实例关联的变量数据.
     *
     * @param processInstanceId
     * @param executionInstanceId  可以从 @see {@link TaskInstance#getExecutionInstanceId()} 获取
     */
     List<VariableInstance> findList(String processInstanceId, String executionInstanceId);
}

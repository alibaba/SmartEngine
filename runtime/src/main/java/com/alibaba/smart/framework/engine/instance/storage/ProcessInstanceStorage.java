package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.PaginateRequest;
import com.alibaba.smart.framework.engine.service.param.ProcessInstanceParam;

/**
 * 流程实例存储
 */
public interface ProcessInstanceStorage {

    /**
     * 保存流程实例
     *
     * @param processInstance 流程实例
     */
    ProcessInstance insert(ProcessInstance processInstance);

    ProcessInstance update(ProcessInstance processInstance);


    /**
     * 加载流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 实例
     */
    ProcessInstance find(Long processInstanceId);

    List<ProcessInstance> queryProcessInstanceList(ProcessInstanceParam processInstanceParam);


    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void remove(Long processInstanceId);


}

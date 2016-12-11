package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 流程实例存储
 */
public interface ProcessInstanceStorage {

    /**
     * 保存流程实例
     *
     * @param processInstance 流程实例
     */
    ProcessInstance save(ProcessInstance processInstance);

    /**
     * 加载流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 实例
     */
    ProcessInstance find(Long processInstanceId);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void remove(Long processInstanceId);


}

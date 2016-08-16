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
    ProcessInstance find(String processInstanceId);

    /**
     * 加载执行实例下子流程
     *
     * @param activityInstanceId 活动实例ID
     * @return 实例
     */
    ProcessInstance findSubProcess(String activityInstanceId);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    void remove(String processInstanceId);







}

package com.alibaba.smart.framework.engine.instance.store;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * 实例存储
 * Created by ettear on 16-4-13.
 */
public interface ProcessInstanceStorage {

    /**
     * 保存实例
     * @param instance 实例
     */
    ProcessInstance save(ProcessInstance instance);

    /**
     * 加载实例
     * @param instanceId 实例ID
     * @return 实例
     */
    ProcessInstance load(String instanceId);

    /**
     * 删除实例
     * @param instanceId 实例ID
     */
    void remove(String instanceId);
}

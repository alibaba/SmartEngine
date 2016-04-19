package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

/**
 * 流程容器
 * Created by ettear on 16-4-19.
 */
public interface ProcessContainer {

    /**
     * 添加流程
     *
     * @param processComponent 流程
     */
    void add(RuntimeProcessComponent processComponent);

    /**
     * 获取流程
     *
     * @param processId 流程ID
     * @param version   版本
     * @return 流程
     */
    RuntimeProcessComponent get(String processId, String version);
}

package com.alibaba.smart.framework.engine.instance.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 流程实例工厂 Created by ettear on 16-4-20.
 */
public interface ProcessInstanceFactory {

    /**
     * 创建流程实例
     *
     * @return 流程实例
     */
    ProcessInstance create(ExecutionContext executionContext);


}

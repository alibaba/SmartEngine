package com.alibaba.smart.framework.engine.instance.factory;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.param.ExecutionParam;

/**
 * 执行实例工厂 Created by ettear on 16-4-20.
 */
public interface ExecutionInstanceFactory {

    /**
     * 创建执行实例
     *
     * @return 执行实例
     */
    ExecutionInstance create(ActivityInstance activityInstance);


    ExecutionInstance recovery(ExecutionParam executionParam);

    String toDatabase(ExecutionInstance executionInstance);
}

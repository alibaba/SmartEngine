package com.alibaba.smart.framework.engine.instance.factory;

import com.alibaba.smart.framework.engine.model.TaskInstance;

/**
 * 任务实例工厂 Created by ettear on 16-4-20.
 */
public interface TaskInstanceFactory {

    /**
     * 创建任务实例
     *
     * @return 任务实例
     */
    TaskInstance create();
}

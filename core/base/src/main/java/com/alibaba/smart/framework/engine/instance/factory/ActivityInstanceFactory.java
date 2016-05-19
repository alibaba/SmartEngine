package com.alibaba.smart.framework.engine.instance.factory;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;

/**
 * 活动实例工厂
 * Created by ettear on 16-4-20.
 */
public interface ActivityInstanceFactory {

    /**
     * 创建活动实例
     *
     * @return 活动实例
     */
    ActivityInstance create();
}

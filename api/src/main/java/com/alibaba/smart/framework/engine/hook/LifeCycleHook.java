package com.alibaba.smart.framework.engine.hook;

/**
 * 生命周期监听 Created by ettear on 16-4-13.
 */
public interface LifeCycleHook {

    /**
     * This method will be invoked when started.
     */
    void start();

    /**
     * This method will be invoked when stopped
     */
    void stop();
}

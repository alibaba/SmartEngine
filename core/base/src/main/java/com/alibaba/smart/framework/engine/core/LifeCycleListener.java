package com.alibaba.smart.framework.engine.core;

/**
 * LifeCycleListener
 * Created by ettear on 16-4-13.
 */
public interface LifeCycleListener {
    /**
     * This method will be invoked when started.
     */
    void start();

    /**
     * This method will be invoked when stopped
     */
    void stop();
}

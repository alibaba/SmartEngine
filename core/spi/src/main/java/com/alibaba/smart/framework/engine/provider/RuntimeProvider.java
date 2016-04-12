package com.alibaba.smart.framework.engine.provider;

/**
 * Created by ettear on 16-4-11.
 */
public interface RuntimeProvider {
    /**
     * This method will be invoked when the corresponding flow component is started.
     */
    void start();

    /**
     * This method will be invoked when the corresponding flow component is stopped
     */
    void stop();
}

package com.alibaba.smart.framework.engine.assembly;

/**
 * 基础装配模型
 * Created by ettear on 16-4-11.
 */
public interface Base {

    /**
     *
     * @return
     */
    boolean isUnresolved();
    void setUnresolved(boolean unresolved);
}

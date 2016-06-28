package com.alibaba.smart.framework.engine.assembly;

import java.io.Serializable;

/**
 * 基础装配模型 Created by ettear on 16-4-11.
 */
public interface Base extends Serializable {

    /**
     * @return
     */
    boolean isUnresolved();

    void setUnresolved(boolean unresolved);
}

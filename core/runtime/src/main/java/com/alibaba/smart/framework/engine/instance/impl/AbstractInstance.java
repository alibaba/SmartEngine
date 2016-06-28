package com.alibaba.smart.framework.engine.instance.impl;

import lombok.Data;

import com.alibaba.smart.framework.engine.instance.Instance;

/**
 * 抽象实例 Created by ettear on 16-4-19.
 */
@Data
public abstract class AbstractInstance implements Instance {

    /**
     * 实例Id
     */
    private String instanceId;
}

package com.alibaba.smart.framework.engine.instance.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.instance.TransitionInstance;

/**
 * 默认关联实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultTransitionInstance extends AbstractInstance implements TransitionInstance {

    private String transitionId;
    private String sourceActivityInstanceId;
    private String targetActivityInstanceId;
}

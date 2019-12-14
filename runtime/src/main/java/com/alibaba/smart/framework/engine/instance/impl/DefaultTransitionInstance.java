package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;

import lombok.Data;

/**
 * 默认关联实例 Created by ettear on 16-4-19.
 */
@Data
public class DefaultTransitionInstance implements TransitionInstance {

    private static final long serialVersionUID = 8888812970442968263L;
    private String transitionId;
    private String sourceActivityInstanceId;
}

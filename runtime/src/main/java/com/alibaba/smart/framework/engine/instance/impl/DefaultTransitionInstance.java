package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认关联实例 Created by ettear on 16-4-19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DefaultTransitionInstance extends AbstractInstance implements TransitionInstance {

    private static final long serialVersionUID = 8888812970442968263L;
    private String transitionId;
    private Long sourceActivityInstanceId;
    private Long targetActivityInstanceId;
}

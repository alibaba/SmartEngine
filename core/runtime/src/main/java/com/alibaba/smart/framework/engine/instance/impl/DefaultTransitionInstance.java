package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import lombok.Data;

/**
 * Created by ettear on 16-4-19.
 */
@Data
public class DefaultTransitionInstance implements TransitionInstance {
    private String sequenceFlowId;
    private String sourceActivityInstanceId;
}

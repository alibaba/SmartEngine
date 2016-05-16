package com.alibaba.smart.framework.engine.instance.factory;

import java.util.Map;

import com.alibaba.smart.framework.engine.instance.InstanceFact;

/**
 * Created by ettear on 16-5-4.
 */
public interface InstanceFactFactory {
    InstanceFact create();
    InstanceFact create(Map<String,Object> variables);
}

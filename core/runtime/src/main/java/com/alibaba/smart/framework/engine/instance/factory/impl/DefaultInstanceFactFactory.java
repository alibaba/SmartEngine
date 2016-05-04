package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.InstanceFact;
import com.alibaba.smart.framework.engine.instance.factory.InstanceFactFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultInstanceFact;

import java.util.Map;

/**
 * Created by ettear on 16-5-4.
 */
public class DefaultInstanceFactFactory implements InstanceFactFactory {

    @Override
    public InstanceFact create() {
        return new DefaultInstanceFact();
    }

    @Override
    public InstanceFact create(Map<String, Object> variables) {
        InstanceFact fact=this.create();
        if(null!=variables && !variables.isEmpty()) {
            fact.putAll(variables);
        }
        return fact;
    }
}

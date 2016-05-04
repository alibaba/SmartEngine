package com.alibaba.smart.framework.engine.context.factory.impl;

import com.alibaba.smart.framework.engine.context.Fact;
import com.alibaba.smart.framework.engine.context.factory.FactFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultFact;

import java.util.Map;

/**
 * Created by ettear on 16-5-4.
 */
public class DefaultFactFactory implements FactFactory {

    @Override
    public Fact create() {
        return new DefaultFact();
    }

    @Override
    public Fact create(Map<String, Object> variables) {
        Fact fact=this.create();
        if(null!=variables && !variables.isEmpty()) {
            fact.putAll(variables);
        }
        return fact;
    }
}

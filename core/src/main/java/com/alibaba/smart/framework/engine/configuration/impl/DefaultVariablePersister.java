package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.VariablePersister;

import java.util.Set;

/** Created by 高海军 帝奇 74394 on 2017 October 07:09. */
public class DefaultVariablePersister implements VariablePersister {

    @Override
    public boolean isPersisteVariableInstanceEnabled() {
        return false;
    }

    @Override
    public Set<String> getBlockList() {
        return null;
    }

    @Override
    public String serialize(Object value) {
        return null;
    }

    @Override
    public Object deserialize(String key, String type, String value) {
        return null;
    }
}

package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;


public class LazyPersistenceEnabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "lazyPersistenceEnabled";
    }

    @Override
    public Object getData() {
        return null;
    }
}
package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public class DisabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getId() {
        return "disabled";
    }
}
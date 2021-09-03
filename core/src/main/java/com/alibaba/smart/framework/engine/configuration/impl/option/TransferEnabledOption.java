package com.alibaba.smart.framework.engine.configuration.impl.option;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public class TransferEnabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "transferEnabled";
    }

    @Override
    public Object getData() {
        return null;
    }
}
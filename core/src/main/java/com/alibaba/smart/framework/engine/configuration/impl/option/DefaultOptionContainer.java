package com.alibaba.smart.framework.engine.configuration.impl.option;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.OptionContainer;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public class DefaultOptionContainer implements OptionContainer {

    private Map<String,ConfigurationOption> map = new HashMap<String,ConfigurationOption>();

    @Override
    public void put(ConfigurationOption configurationOption) {
        map.put(configurationOption.getId(),configurationOption);
    }

    @Override
    public ConfigurationOption get(String id) {
        ConfigurationOption configurationOption =   map.get(id);
        if(null == configurationOption){
            return ConfigurationOption.DISABLED_OPTION;
        }
        return configurationOption;
    }


}
package com.alibaba.smart.framework.engine.configuration;

/**
 * Created by 高海军 帝奇 74394 on  2020-02-15 21:40.
 */
public interface OptionContainer {



    void put(ConfigurationOption configurationOption);

    ConfigurationOption get(String id);

}
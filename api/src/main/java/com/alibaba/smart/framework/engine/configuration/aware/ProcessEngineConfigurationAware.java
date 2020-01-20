package com.alibaba.smart.framework.engine.configuration.aware;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

/**
 * Created by 高海军 帝奇 74394 on  2019-12-30 15:38.
 */
public interface ProcessEngineConfigurationAware {

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);
}
package com.alibaba.smart.framework.process.configuration.impl;

import java.util.List;

import com.alibaba.smart.framework.process.configuration.ProcessEngineConfiguration;


public class DefaultProcessEngineConfiguration implements ProcessEngineConfiguration{
    
    private List<String> processDefinitions;
    
    
    //TODO 抽象成resources
    public DefaultProcessEngineConfiguration(List<String> processDefinitions) {
        this.processDefinitions = processDefinitions;
    }

    @Override
    public List<String> getProcessDefinitions() {
        return processDefinitions;
    }

}

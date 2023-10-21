package com.alibaba.smart.framework.engine.configuration.impl;


import com.alibaba.smart.framework.engine.configuration.PvmActivityTask;
import com.alibaba.smart.framework.engine.configuration.PvmActivityTaskFactory;

public  class DefaultPvmActivityTaskFactory implements PvmActivityTaskFactory {

    public DefaultPvmActivityTask create(Object... args){
        return new DefaultPvmActivityTask(args);
    }

}

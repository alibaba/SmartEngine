package com.alibaba.smart.framework.engine.configuration;


import com.alibaba.smart.framework.engine.configuration.impl.DefaultPvmActivityTask;

public  interface PvmActivityTaskFactory {

    PvmActivityTask create(Object... args);

}

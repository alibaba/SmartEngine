package com.alibaba.smart.framework.engine.configuration.impl;


import com.alibaba.smart.framework.engine.configuration.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public class DefaultIdGenerator implements IdGenerator{

    private static AtomicLong executionId = new AtomicLong(20000000000L);

    //TODO 尽量做到不重复,不能在生产环境中使用。
    @Override
    public String getId() {
        return String.valueOf(executionId.getAndIncrement());
    }
}

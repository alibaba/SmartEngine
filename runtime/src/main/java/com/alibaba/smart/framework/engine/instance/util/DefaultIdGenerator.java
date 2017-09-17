package com.alibaba.smart.framework.engine.instance.util;


import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public class DefaultIdGenerator implements IdGenerator{

    private static AtomicLong executionId = new AtomicLong();

    //TODO 尽量做到不重复
    @Override
    public Long getId() {
        return Long.valueOf(executionId.getAndIncrement());
    }
}

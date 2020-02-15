package com.alibaba.smart.framework.engine.test;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  00:02.
 */
public class AliPayIdGenerator implements IdGenerator {

    private AtomicLong atomicLong = new AtomicLong(233L);

    @Override
    public String getId() {
        long l = atomicLong.addAndGet(100L);
        return l + "";
    }
}

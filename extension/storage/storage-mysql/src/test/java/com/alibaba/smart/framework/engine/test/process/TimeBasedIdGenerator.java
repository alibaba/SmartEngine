package com.alibaba.smart.framework.engine.test.process;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  00:02.
 */
public class TimeBasedIdGenerator implements IdGenerator {

    private  static AtomicLong temp = new AtomicLong(0);

    @Override
    public String getId() {
        return System.currentTimeMillis()+temp.getAndIncrement()+"";
    }
}

package com.alibaba.smart.framework.engine.test.process.sequece;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public class RandomIdGenerator implements IdGenerator{


    private static AtomicLong executionId = new AtomicLong(Math.abs(new Random().nextLong()));

    //TODO 尽量做到不重复,不能在生产环境中使用。
    @Override
    public String getId() {
        return String.valueOf(executionId.incrementAndGet());
    }
}

package com.alibaba.smart.framework.engine.test.storage.dual.helper;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.model.instance.Instance;

public class TimeBasedIdGenerator implements IdGenerator {

    private static AtomicLong temp = new AtomicLong(0);

    @Override
    public void generate(Instance instance) {
        String s = System.currentTimeMillis() + temp.getAndIncrement() + "";
        instance.setInstanceId(s);
    }
}

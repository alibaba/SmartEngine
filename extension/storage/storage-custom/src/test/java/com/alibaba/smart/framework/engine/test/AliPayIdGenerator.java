package com.alibaba.smart.framework.engine.test;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  00:02.
 */
public class AliPayIdGenerator implements IdGenerator {

    private AtomicLong atomicLong = new AtomicLong(233L);

    @Override
    public void generate(Instance instance) {
        long l = atomicLong.addAndGet(100L);
        String s = l + "";
        instance.setInstanceId(s);
//        return s;
    }
}

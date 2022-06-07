package com.alibaba.smart.framework.engine.test.process.helper.sequece;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 *
 * 不建议在生产环境中使用。
 */
public class RandomIdGenerator implements IdGenerator{


    private static AtomicLong executionId = new AtomicLong(Math.abs(new Random().nextLong()));

    @Override
    public void generate(Instance instance) {
        String s = String.valueOf(executionId.incrementAndGet());
        instance.setInstanceId(s);
//        return s;
    }
}

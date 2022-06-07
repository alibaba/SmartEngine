package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.UUID;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public class DefaultIdGenerator implements IdGenerator{

    // 不建议在生产环境中使用。
    @Override
    public void generate(Instance instance) {

        String  randomUUID =  UUID.randomUUID().toString();

        instance.setInstanceId(randomUUID);
//        return  randomUUID;

    }
}

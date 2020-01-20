package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.UUID;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  23:17.
 */
public class DefaultIdGenerator implements IdGenerator{

    // 不建议在生产环境中使用。
    @Override
    public String getId() {

        String  randomUUID =  UUID.randomUUID().toString();
        return  randomUUID;

    }
}

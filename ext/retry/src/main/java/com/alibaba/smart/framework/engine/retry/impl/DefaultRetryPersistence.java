package com.alibaba.smart.framework.engine.retry.impl;

import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;

import java.util.Map;

/**
 * @author zhenhong.tzh
 * @date 2019-06-03
 */
public class DefaultRetryPersistence implements RetryPersistence {

    @Override
    public String serialize(Map<String, Object> params) {
        return null;
    }

    @Override
    public Map<String, Object> deserialize(String params) {
        return null;
    }
}

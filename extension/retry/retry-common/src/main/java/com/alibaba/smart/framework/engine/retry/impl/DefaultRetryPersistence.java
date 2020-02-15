package com.alibaba.smart.framework.engine.retry.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;

/**
 * @author zhenhong.tzh
 * @date 2019-06-03
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = RetryPersistence.class)

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

package com.alibaba.smart.framework.engine.retry.service.command;

import java.util.Map;

/**
 * @author zhenhong.tzh
 * @date 2019-06-03
 */
public interface RetryPersistence {

    /**
     * 把Map形式的param转换成string
     *
     * @param params 入参
     * @return
     */
    String serialize(Map<String, Object> params);

    /**
     * 把string形式的param转换成流程执行所需参数
     *
     * @param params 入参
     * @return
     */
    Map<String, Object> deserialize(String params);
}

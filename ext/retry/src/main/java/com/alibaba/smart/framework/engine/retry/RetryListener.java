package com.alibaba.smart.framework.engine.retry;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;

/**
 * 重试消息监听器
 *
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public interface RetryListener {

    /**
     * 监听retry表的更新动作
     *
     * @param retryRecord 重试对象
     */
    void onMessage(RetryRecord retryRecord);
}

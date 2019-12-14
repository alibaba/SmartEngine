package com.alibaba.smart.framework.engine.retry.model.instance;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public interface RetryRecord extends Instance {

    /**
     * 获取重试次数
     *
     * @return 已重试的次数
     */
    int getRetryTimes();

    /**
     * 设置重试次数
     *
     * @param retryTimes 已重试的次数
     */
    void setRetryTimes(int retryTimes);

    /**
     * 是否重试成功
     *
     * @return 是否成功
     */
    boolean isRetrySuccess();

    /**
     * 设置重试成功标记
     *
     * @param success 是否成功
     */
    void setRetrySuccess(boolean success);

    /**
     * 获取当前的执行参数
     *
     * @return
     */
    Map<String, Object> getRequestParams();

    /**
     * 重试所需要的参数
     *
     * @param params
     */
    void setRequestParams(Map<String, Object> params);
}

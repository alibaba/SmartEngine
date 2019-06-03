package com.alibaba.smart.framework.engine.retry.model.instance;

import lombok.Data;

import java.util.Map;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
@Data
public class DefaultRetryRecordInstance implements RetryRecord {
    private String instanceId;
    private int retryTimes;
    private boolean retrySuccess;
    private Map<String, Object> requestParams;
}

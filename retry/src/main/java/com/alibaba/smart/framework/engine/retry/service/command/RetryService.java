package com.alibaba.smart.framework.engine.retry.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public interface RetryService {

    boolean save(RetryRecord retryRecord);

    ProcessInstance retry(RetryRecord retryRecord);
}

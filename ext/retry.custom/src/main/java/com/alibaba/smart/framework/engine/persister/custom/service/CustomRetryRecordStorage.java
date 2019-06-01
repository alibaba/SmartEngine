package com.alibaba.smart.framework.engine.persister.custom.service;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import java.util.List;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

/**
 * @author zhenhong.tzh
 * @date 2019-05-27
 */
public class CustomRetryRecordStorage implements RetryRecordStorage {

    @Override
    public boolean insertOrUpdate(RetryRecord retryRecord) {
        return true;
    }
}

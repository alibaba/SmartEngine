package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import org.springframework.stereotype.Repository;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
@Repository
public interface RetryRecordDAO {

    RetryRecord queryByInstanceId(String instanceId);

    Long insert(RetryRecord retryRecord);

    Long update(RetryRecord retryRecord);
}

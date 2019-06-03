package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.RetryRecordEntity;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import org.springframework.stereotype.Repository;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
@Repository
public interface RetryRecordDAO {

    RetryRecordEntity queryByInstanceId(String instanceId);

    Long insert(RetryRecordEntity retryRecordEntity);

    Long update(RetryRecordEntity retryRecordEntity);
}

package com.alibaba.smart.framework.engine.persister.database.service;

import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.RetryRecordDAO;
import com.alibaba.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public class RelationshipDatabaseRetryRecordStorage implements RetryRecordStorage {

    @Override
    public boolean insertOrUpdate(RetryRecord retryRecord) {
        RetryRecordDAO activityInstanceDAO = (RetryRecordDAO)SpringContextUtil.getBean("retryRecordDAO");

        RetryRecord retryRecordDO = activityInstanceDAO.queryByInstanceId(retryRecord.getInstanceId());
        long count = 0L;
        if (retryRecordDO == null) {
            count = activityInstanceDAO.insert(retryRecord);
        } else {
            count = activityInstanceDAO.update(retryRecord);
        }
        return count > 0;
    }
}

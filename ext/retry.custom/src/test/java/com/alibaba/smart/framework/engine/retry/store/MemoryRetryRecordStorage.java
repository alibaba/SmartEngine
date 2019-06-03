package com.alibaba.smart.framework.engine.retry.store;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.retry.RetryExtensionPoint;
import com.alibaba.smart.framework.engine.retry.RetryListener;
import com.alibaba.smart.framework.engine.retry.instance.storage.RetryRecordStorage;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryPersistence;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public class MemoryRetryRecordStorage implements RetryRecordStorage {

    private RetryRecord retryRecord;

    public MemoryRetryRecordStorage() {
        /**
         * 模拟DTS定时扫描重试表
         */
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (retryRecord != null && !retryRecord.isRetrySuccess()) {
                    notifyRetry(retryRecord);
                }
            }

            private void notifyRetry(RetryRecord retryRecord) {
                ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
                processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());
                SmartEngine smartEngine = new DefaultSmartEngine();
                smartEngine.init(processEngineConfiguration);
                RetryExtensionPoint retryExtensionPoint = processEngineConfiguration.getExtensionPointRegistry()
                    .getExtensionPoint(RetryExtensionPoint.class);
                RetryListener retryListener = retryExtensionPoint.getExtensionPoint(RetryListener.class);
                retryListener.onMessage(retryRecord);
            }
        };
        //new Timer().schedule(timerTask, 1000);
    }

    @Override
    public RetryRecord find(String instanceId, RetryPersistence retryPersistence) {
        return retryRecord;
    }

    @Override
    public boolean insert(RetryRecord retryRecord, RetryPersistence retryPersistence) {
        this.retryRecord = retryRecord;
        return true;
    }

    @Override
    public boolean update(RetryRecord retryRecord, RetryPersistence retryPersistence) {
        this.retryRecord = retryRecord;
        return true;
    }
}

package com.alibaba.smart.framework.engine.persister.database.entity;

import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RetryRecordEntity extends BaseProcessEntity implements RetryRecord {

    private String instanceId;
    private int retryTimes;
    private boolean retrySuccess;
    private String requestParams;

    @Override
    public Map<String, Object> getRequestParams() {
        // FIXME string转object
        return null;
    }

    @Override
    public void setRequestParams(Map<String, Object> params) {

    }
}

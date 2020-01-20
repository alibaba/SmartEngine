package com.alibaba.smart.framework.engine.persister.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zhenhong.tzh
 * @date 2019-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RetryRecordEntity extends BaseProcessEntity {

    private String instanceId;
    private int retryTimes;
    private boolean retrySuccess;
    private String requestParams;
}

package com.alibaba.smart.framework.engine.test.retry.delegation;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 November  14:41.
 */
@Data
public class DiqiBean implements  java.io.Serializable{
    private Long   orderId;
    private String currentActivityId;
    private String msg;
}
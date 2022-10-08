package com.alibaba.smart.framework.engine.test.parallelgateway.orchestration;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-07 10:52.
 */
@Data
@AllArgsConstructor
public class ThreadExecutionResult {

    private Long threadId;
    private Object payload;
}
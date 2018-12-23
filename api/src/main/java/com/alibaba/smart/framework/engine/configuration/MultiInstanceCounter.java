package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.SmartEngine;

/**
 * Created by 高海军 帝奇 74394 on 2017 October.14  10:41.
 */
public interface MultiInstanceCounter {

    Long countPassedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                       SmartEngine smartEngine);

    Long countRejectedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                         SmartEngine smartEngine);

}

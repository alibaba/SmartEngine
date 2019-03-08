package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.SmartEngine;

public interface ItemApproveMultiInstanceCounter {

    Long countPassedTaskItemInstanceNumber(String processInstanceId, String activityInstanceId, String subBizId, SmartEngine smartEngine);

    Long countRejectedTaskItemInstanceNumber(String processInstanceId, String activityInstanceId, String subBizId, SmartEngine smartEngine);

    Long countAllTaskItemInstanceNumber(String processInstanceId, String activityInstanceId, String subBizId, SmartEngine smartEngine);
}

package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.MultiInstanceCounter;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultMultiInstanceCounter implements MultiInstanceCounter {


    @Override
    public Long countPassedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                              SmartEngine smartEngine) {
      TaskQueryService taskQueryService = smartEngine.getTaskQueryService();

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(activityInstanceId);
        taskInstanceQueryParam.setTag(FullMultiInstanceTest.AGREE);
        Long count  = taskQueryService.count(taskInstanceQueryParam);
        return  count;

    }

    @Override
    public Long countRejectedTaskInstanceNumber(String processInstanceId, String activityInstanceId,
                                                SmartEngine smartEngine) {
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(activityInstanceId);
        taskInstanceQueryParam.setTag(FullMultiInstanceTest.DISAGREE);
        Long count  = taskQueryService.count(taskInstanceQueryParam);
        return  count;

    }

}

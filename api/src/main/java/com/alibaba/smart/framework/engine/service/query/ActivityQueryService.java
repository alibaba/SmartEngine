package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.List;

/**
 * 流程节点查询服务，主要用来显示流程操作轨迹。
 *
 * Created by 高海军 帝奇 74394 on 2016 December  11:07.
 */
public interface ActivityQueryService {

    /**
     * 默认按照时间降序
     * @param processInstanceId
     * @return
     */
    List<ActivityInstance> findAll(Long processInstanceId);

    List<ActivityInstance> findAll(Long processInstanceId,boolean asc);

}

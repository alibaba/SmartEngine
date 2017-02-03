package com.alibaba.smart.framework.engine.common.persister;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  08:58.
 */
public interface PersisterStrategy {

    ProcessInstance persist(ProcessInstance processInstance,Map<String, Object> request);
}

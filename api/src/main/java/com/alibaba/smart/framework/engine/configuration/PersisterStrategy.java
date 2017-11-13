package com.alibaba.smart.framework.engine.configuration;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  08:58.
 */
public interface PersisterStrategy {

    String insert(ProcessInstance  processInstance);

    String update(ProcessInstance  processInstance);


    ProcessInstance getProcessInstance(Long processInstanceId);

    ProcessInstance getProcessInstanceByExecutionInstanceId(Long executionInstanceId);


    }

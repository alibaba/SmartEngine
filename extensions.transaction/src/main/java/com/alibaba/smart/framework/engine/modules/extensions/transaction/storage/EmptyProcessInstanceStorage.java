package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import java.util.List;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyProcessInstanceStorage implements ProcessInstanceStorage {
    @Override
    public ProcessInstance insert(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance findOne(Long processInstanceId) {
        return null;
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam) {
        return null;
    }


    @Override
    public void remove(Long processInstanceId) {
    }
}

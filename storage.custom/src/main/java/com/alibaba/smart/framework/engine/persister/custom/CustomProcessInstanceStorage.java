package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class CustomProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance instance) {
        PersisterSession.currentSession().putProcessInstance(instance);
        return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstanceVar) {
        PersisterSession.currentSession().putProcessInstance(processInstanceVar);
        return processInstanceVar;
    }

    @Override
    public ProcessInstance findOne(Long instanceId) {
        return  PersisterSession.currentSession().getProcessInstance(instanceId);
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam) {
        return null;
    }



    @Override
    public void remove(Long instanceId) {
        PersisterSession.currentSession().getProcessInstances().remove(instanceId);
    }
}

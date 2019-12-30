package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ProcessInstanceStorage.class)

public class CustomProcessInstanceStorage implements ProcessInstanceStorage {


    @Override
    public ProcessInstance insert(ProcessInstance instance,
                                  ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession.currentSession().putProcessInstance(instance);
        return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstanceVar,
                                  ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession.currentSession().putProcessInstance(processInstanceVar);
        return processInstanceVar;
    }

    @Override
    public ProcessInstance findOne(String instanceId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        return  PersisterSession.currentSession().getProcessInstance(instanceId);
    }

    @Override
    public ProcessInstance findOneForUpdate(String instanceId,
                                            ProcessEngineConfiguration processEngineConfiguration) {
        return  PersisterSession.currentSession().getProcessInstance(instanceId);
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam,
                                                          ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }


    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession.currentSession().getProcessInstances().remove(instanceId);
    }
}

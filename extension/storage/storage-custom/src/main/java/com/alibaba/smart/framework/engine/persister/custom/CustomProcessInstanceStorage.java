package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.Instance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public ProcessInstance findOne(String instanceId, String tenantId,
                                   ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(instanceId);
        processEngineConfiguration.getProcessDefinitionKeyGenerator().generate(processInstance);
        return processInstance;
    }

    @Override
    public ProcessInstance findOneForUpdate(String instanceId, String tenantId,
                                            ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(instanceId);
        processEngineConfiguration.getProcessDefinitionKeyGenerator().generate(processInstance);
        return processInstance;
    }

    @Override
    public List<ProcessInstance> queryProcessInstanceList(ProcessInstanceQueryParam processInstanceQueryParam,
                                                          ProcessEngineConfiguration processEngineConfiguration) {
        Collection<ProcessInstance> procInsts = PersisterSession.currentSession().getProcessInstances().values();
        if (procInsts == null || procInsts.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProcessInstance> list = procInsts.stream().filter(pi -> {
                    if (processInstanceQueryParam.getStartUserId() != null) {
                        return StringUtil.isNotEmpty(pi.getStartUserId()) && pi.getStartUserId().equals(processInstanceQueryParam.getStartUserId());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getStatus() != null) {
                        return pi.getStatus() != null && pi.getStatus().name().equals(processInstanceQueryParam.getStatus());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getProcessDefinitionIdAndVersion() != null) {
                        return StringUtil.isNotEmpty(pi.getProcessDefinitionIdAndVersion()) && pi.getProcessDefinitionIdAndVersion().equals(processInstanceQueryParam.getProcessDefinitionIdAndVersion());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getProcessDefinitionType() != null) {
                        return StringUtil.isNotEmpty(pi.getProcessDefinitionType()) && pi.getProcessDefinitionType().equals(processInstanceQueryParam.getProcessDefinitionType());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getParentInstanceId() != null) {
                        return StringUtil.isNotEmpty(pi.getParentInstanceId()) && pi.getParentInstanceId().equals(processInstanceQueryParam.getParentInstanceId());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getProcessStartTime() != null) {
                        return pi.getStartTime() != null && pi.getStartTime().after(processInstanceQueryParam.getProcessStartTime());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getProcessEndTime() != null) {
                        return pi.getCompleteTime() != null && pi.getCompleteTime().before(processInstanceQueryParam.getProcessEndTime());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getProcessInstanceIdList() != null) {
                        return pi.getInstanceId() != null && processInstanceQueryParam.getProcessInstanceIdList().contains(pi.getInstanceId());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getId() != null) {
                        return pi.getInstanceId() != null && processInstanceQueryParam.getId().equals(pi.getInstanceId());
                    }
                    return true;
                }).collect(Collectors.toList()).stream().filter(pi -> {
                    if (processInstanceQueryParam.getTenantId() != null) {
                        return pi.getTenantId() != null && processInstanceQueryParam.getTenantId().equals(pi.getTenantId());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        if (processInstanceQueryParam.getPageOffset() != null && processInstanceQueryParam.getPageSize() != null) {
            list = list.stream().sorted(Comparator.comparing(Instance::getInstanceId)).collect(Collectors.toList())
                    .subList(processInstanceQueryParam.getPageOffset(), processInstanceQueryParam.getPageOffset() + processInstanceQueryParam.getPageSize());
            list.forEach(pi -> {
                processEngineConfiguration.getProcessDefinitionKeyGenerator().generate(pi);
            });
        }

        return list;
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        Collection<ProcessInstance> procInsts = PersisterSession.currentSession().getProcessInstances().values();
        if (procInsts == null || procInsts.isEmpty()) {
            return 0L;
        }
        return Long.valueOf(procInsts.size());
    }


    @Override
    public void remove(String instanceId, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession.currentSession().getProcessInstances().remove(instanceId);
    }
}

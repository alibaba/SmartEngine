package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskAssigneeStorage.class)

public class CustomTaskAssigneeInstanceStorage implements TaskAssigneeStorage {



    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId,String tenantId,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,String tenantId,
                                                                              ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskAssigneeInstance> findPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                                                  ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public Long countPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance update(String taskAssigneeId, String assigneeId,String tenantId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeId,String tenantId,
                                        ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public void remove(String taskAssigneeId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {

    }

    @Override
    public void removeAll(String taskInstanceId,String tenantId, ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);

    }
}

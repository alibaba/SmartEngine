package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */

@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceStorage.class)

public class CustomTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance instance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public TaskInstance update(TaskInstance instance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public TaskInstance find(String instanceId,String tenantId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public void remove(String instanceId,String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }
}

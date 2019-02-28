package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskItemQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

public interface TaskItemInstanceStorage {

    List<TaskItemInstance> findTaskItemByProcessInstanceIdAndStatus(TaskItemInstanceQueryParam taskItemInstanceQueryParam,
                                                                ProcessEngineConfiguration processEngineConfiguration);

    List<TaskItemInstance> findPendingTaskItemList(PendingTaskItemQueryParam pendingTaskItemQueryParam,
                                               ProcessEngineConfiguration processEngineConfiguration);

    Long countPendingTaskItemList(PendingTaskItemQueryParam pendingTaskItemQueryParam,
                              ProcessEngineConfiguration processEngineConfiguration);

    List<TaskItemInstance> findTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                                  ProcessEngineConfiguration processEngineConfiguration);

    Long countTaskItemListByAssignee(TaskItemInstanceQueryByAssigneeParam param,
                                 ProcessEngineConfiguration processEngineConfiguration);

    List<TaskItemInstance> findTaskList(TaskItemInstanceQueryParam taskInstanceQueryParam,
                                    ProcessEngineConfiguration processEngineConfiguration);

    Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam,
               ProcessEngineConfiguration processEngineConfiguration);

    TaskItemInstance insert(TaskItemInstance taskInstance,
                        ProcessEngineConfiguration processEngineConfiguration);

    TaskItemInstance update(TaskItemInstance taskInstance,
                        ProcessEngineConfiguration processEngineConfiguration);

    int updateFromStatus(TaskItemInstance taskInstance, String fromStatus,
                         ProcessEngineConfiguration processEngineConfiguration);

    TaskItemInstance find(String taskItemInstanceId,
                      ProcessEngineConfiguration processEngineConfiguration);

    void remove(String taskItemInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

}

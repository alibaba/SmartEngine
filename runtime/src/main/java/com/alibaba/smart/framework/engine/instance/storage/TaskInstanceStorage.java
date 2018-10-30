package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;


public interface TaskInstanceStorage {

    List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                            ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration);

    Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                 ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                              ProcessEngineConfiguration processEngineConfiguration);

    Integer countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                    ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                    ProcessEngineConfiguration processEngineConfiguration);

    Integer count(TaskInstanceQueryParam taskInstanceQueryParam,
                  ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance insert(TaskInstance taskInstance,
                        ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance update(TaskInstance taskInstance,
                        ProcessEngineConfiguration processEngineConfiguration);

    int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                         ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance find(String taskInstanceId,
                      ProcessEngineConfiguration processEngineConfiguration);

    void remove(String taskInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

}

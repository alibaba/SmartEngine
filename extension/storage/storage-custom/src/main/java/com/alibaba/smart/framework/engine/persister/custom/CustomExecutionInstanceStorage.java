package com.alibaba.smart.framework.engine.persister.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;

import static com.alibaba.smart.framework.engine.persister.common.constant.StorageConstant.NOT_IMPLEMENT_INTENTIONALLY;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceStorage.class)
public class CustomExecutionInstanceStorage implements ExecutionInstanceStorage {

    @Override
    public void insert(ExecutionInstance instance,
                       ProcessEngineConfiguration processEngineConfiguration) {
    }

    @Override
    public void update(ExecutionInstance executionInstance,
                       ProcessEngineConfiguration processEngineConfiguration) {

    }

    @Override
    public ExecutionInstance find(String instanceId,
                                  ProcessEngineConfiguration processEngineConfiguration) {

        Collection<ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances().values();

        boolean matched = false;

        ExecutionInstance executionInstance = null;

        for (ProcessInstance processInstance : processInstances) {

            List<ActivityInstance> activityInstances = processInstance.getActivityInstances();

            if (null == activityInstances || activityInstances.isEmpty()) {

                // do nothing , cause exception.
            } else {
                int size = activityInstances.size();
                for (int i = size - 1; i >= 0; i--) {
                    ActivityInstance activityInstance = activityInstances.get(i);

                    List<ExecutionInstance> executionInstances =    activityInstance.getExecutionInstanceList();
                    for (ExecutionInstance tempExecutionInstance : executionInstances) {
                        if (null != tempExecutionInstance && tempExecutionInstance.getInstanceId().equals(instanceId)) {
                            executionInstance = tempExecutionInstance;
                            matched = true;
                            break;

                        }
                    }

                }

            }
            if (matched) {
                break;
            }
        }

        if(!matched){
            throw new EngineException("No ExecutionInstance found for id : "+instanceId);
        }

        return executionInstance;

    }

    @Override
    public ExecutionInstance findWithShading(String processInstanceId, String executionInstanceId,
                                             ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);

    }

    @Override
    public void remove(String instanceId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        throw new EngineException(NOT_IMPLEMENT_INTENTIONALLY);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId,
                                                       ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(processInstanceId);
        if(null==processInstance){
            return Collections.emptyList();
        }

        return  InstanceUtil.findActiveExecution(processInstance);

     }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,
                                                            ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance = PersisterSession.currentSession().getProcessInstance(processInstanceId);
        if (null == processInstance) {
            return null;
        }
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (null == activityInstances) {
            return null;
        }

        for (ActivityInstance activityInstance : activityInstances) {
            if (activityInstance.getInstanceId().equals(activityInstanceId)) {
                return activityInstance.getExecutionInstanceList();
            }
        }
        return null;
    }

    @Override
    public List<ExecutionInstance> findAll(String processInstanceId, ProcessEngineConfiguration processEngineConfiguration) {
        ProcessInstance processInstance= PersisterSession.currentSession().getProcessInstance(processInstanceId);
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (null == activityInstances || activityInstances.size() == 0) {
            return null;
        }
        List<ExecutionInstance> executionInstances = new ArrayList<ExecutionInstance>();
        for (ActivityInstance activityInstance : activityInstances) {
            executionInstances.addAll(activityInstance.getExecutionInstanceList());
        }
        return executionInstances;
    }
}

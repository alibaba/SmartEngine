package com.alibaba.smart.framework.engine.persister.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  12:12.
 */
public class InstanceSerializerFacade {

    public static String serialize(ProcessInstance processInstance) {
       return InstanceSerializerV1.serialize(processInstance);
    }


    public static ProcessInstance deserializeAll(String serializeString){
        if(serializeString.startsWith("v0")){
            return InstanceSerializer.deserializeAll(serializeString);
        }else if(serializeString.startsWith("v1")){
            return InstanceSerializerV1.deserializeAll(serializeString);
        }else{
            throw new EngineException("unsupport version for deserializeAll:"+serializeString);
        }
    }

    public static ProcessInstance mockSimpleProcessInstance( String  processDefinitionId,String version,InstanceStatus instanceStatus,String processDefinitionActivityId ) {
        ProcessInstance processInstance = new DefaultProcessInstance();
        processInstance.setProcessDefinitionIdAndVersion(processDefinitionId+":"+version);
        processInstance.setProcessDefinitionId(processDefinitionId);
        processInstance.setProcessDefinitionVersion(version);
        processInstance.setStatus(instanceStatus);
        processInstance.setInstanceId(1L);

        ActivityInstance activityInstance = new DefaultActivityInstance();
        activityInstance.setProcessDefinitionActivityId(processDefinitionActivityId);
        activityInstance.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        activityInstance.setInstanceId(1L);

        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        executionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        executionInstance.setProcessDefinitionActivityId(processDefinitionActivityId);
        executionInstance.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        executionInstance.setInstanceId(1L);
        executionInstance.setActive(true);

        List<ExecutionInstance>  executionInstanceList = new ArrayList<ExecutionInstance>();
        executionInstanceList.add(executionInstance);

        activityInstance.setExecutionInstanceList(executionInstanceList);
        processInstance.getActivityInstances().add(activityInstance);

        return processInstance;
    }
}

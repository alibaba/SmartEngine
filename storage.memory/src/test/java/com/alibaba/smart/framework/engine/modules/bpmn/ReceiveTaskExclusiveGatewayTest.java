package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReceiveTaskExclusiveGatewayTest {


    @Test
    public void testExclusive() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-receivetask-exclusive.bpmn20.xml");
        assertEquals(25, processDefinition.getProcess().getElements().size());

        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        Assert.assertNotNull(processInstance);
        List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();

        Assert.assertNotNull(activityInstances);
        int size = activityInstances.size();
        ActivityInstance lastActivityInstance = activityInstances.get(size - 1);

        assertEquals(3, size);


        assertEquals("theTask1", lastActivityInstance.getActivityId());


        ExecutionInstance lastExecutionInstance = lastActivityInstance.getExecutionInstance();
        Assert.assertNotNull(lastExecutionInstance);

        assertEquals(processInstance.getInstanceId(), lastExecutionInstance.getProcessInstanceId());
        assertEquals(lastActivityInstance.getInstanceId(), lastExecutionInstance.getActivityInstanceId());
        assertEquals(lastActivityInstance.getActivityId(), lastExecutionInstance.getActivityId());


        assertEquals("theTask1", lastExecutionInstance.getActivityId());


        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();


        processInstance = executionCommandService.signal(lastExecutionInstance.getInstanceId(), null);
        activityInstances = processInstance.getNewActivityInstances();

        Assert.assertNotNull(activityInstances);
        size = activityInstances.size();
        lastActivityInstance = activityInstances.get(size - 1);
        lastExecutionInstance = lastActivityInstance.getExecutionInstance();
        Assert.assertNotNull(lastExecutionInstance);

        request.put("input", 11);
        processInstance = executionCommandService.signal(lastExecutionInstance.getInstanceId(), request);

        Assert.assertNotNull(processInstance.getCompleteDate());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());


//
//        taskInstanceList =     taskQueryService.findAll(processInstanceId);
//
//        Assert.assertNotNull(taskInstanceList);
//        assertEquals(1,taskInstanceList.size());
//
//          taskInstance = taskInstanceList.get(0);
//        assertEquals("theTask4", taskInstance.getName());
//
//
//        taskCommandService.complete(taskInstance.getInstanceId(), request);
//
//
//        //
//
//        processInstance =    processCommandService.findAll(processInstanceId);
//        assertEquals(InstanceStatus.completed, processInstance.getStatus());


    }


}
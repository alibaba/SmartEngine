package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReceiveTaskProcessTest {


	
	@Test
    public void testUserTaskExclusive() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-receivetask-exclusive.bpmn20.xml");
        assertEquals(25, processDefinition.getProcess().getElements().size());

        ProcessCommandService processCommandService = smartEngine.getProcessService();
        Map<String, Object> request = new HashMap<>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        Assert.assertNotNull(processInstance);
        List<ActivityInstance> activityInstances=  processInstance.getActivityInstances();

        Assert.assertNotNull(activityInstances);

        int size = activityInstances.size();
        assertEquals(3, size);

        ActivityInstance lastActivityInstance = activityInstances.get(size - 1);
        assertEquals("theTask1",lastActivityInstance.getActivityId());


        ExecutionInstance lastExecutionInstance = lastActivityInstance.getExecutionInstance();
        Assert.assertNotNull(lastExecutionInstance);

        assertEquals(processInstance.getInstanceId(),lastExecutionInstance.getProcessInstanceId());
        assertEquals(lastActivityInstance.getInstanceId(),lastExecutionInstance.getActivityInstanceId());
        assertEquals(lastActivityInstance.getActivityId(),lastExecutionInstance.getActivityId());


        assertEquals("theTask1",lastExecutionInstance.getActivityId());


        //1st: create 1st task
        ExecutionCommandService executionCommandService =    smartEngine.getExecutionCommandService();


        request.put("input", 11);

        executionCommandService.signal(lastExecutionInstance.getInstanceId(), request);


//
//        taskInstanceList =     taskQueryService.find(processInstanceId);
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
//        processInstance =    processCommandService.find(processInstanceId);
//        assertEquals(InstanceStatus.completed, processInstance.getStatus());


    }

 

}
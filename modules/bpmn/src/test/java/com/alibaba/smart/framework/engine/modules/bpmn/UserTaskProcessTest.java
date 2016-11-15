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
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserTaskProcessTest {


//	@Test
//	public void testExclusive() throws Exception {
//	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//
//		SmartEngine smartEngine = new DefaultSmartEngine();
//		smartEngine.init(processEngineConfiguration);
//
//		RepositoryCommandService repositoryService = smartEngine
//				.getRepositoryService();
//		ProcessDefinition processDefinition = repositoryService
//				.deploy("test-servicetask-exclusive.bpmn20.xml");
//        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());
//
//		ProcessCommandService processService = smartEngine.getProcessService();
//		Map<String, Object> request = new HashMap<>();
//		request.put("input", 2);
//		ProcessInstance processInstance = processService.start(
//				processDefinition.getId(), processDefinition.getVersion(),
//				request);
//
//		Assert.assertNotNull(processInstance);
//	}
	
	
	@Test
    public void testUserTaskExclusive() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-usertask-exclusive.bpmn20.xml");
        assertEquals(25, processDefinition.getProcess().getElements().size());

        ProcessCommandService processCommandService = smartEngine.getProcessService();
        Map<String, Object> request = new HashMap<>();
        request.put("input", 2);
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

        TaskInstance latestTaskInstance = lastExecutionInstance.getTaskInstance();
        assertNotNull(latestTaskInstance);
        assertEquals("theTask1",latestTaskInstance.getActivityId());


        //1st: create 1st task
        TaskCommandService taskCommandService =    smartEngine.getTaskCommandService();

        TaskInstance taskInstance = latestTaskInstance;

        request.put("input", 4);

        //2nd. create service task ,and then create task
        taskCommandService.complete(taskInstance.getInstanceId(), request);


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

//
//    @Test
//    public void testUserTaskExclusivePersistence() throws Exception {
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//
//        SmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//
//        RepositoryCommandService repositoryCommandService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryCommandService
//                .deploy("test-usertask-exclusive.bpmn20.xml");
//        assertEquals(25, processDefinition.getProcess().getElements().size());
//
//        ProcessCommandService processCommandService = smartEngine.getProcessService();
//        Map<String, Object> request = new HashMap<>();
//        request.put("input", 2);
//        ProcessInstance processInstance = processCommandService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                request);
//
//        Assert.assertNotNull(processInstance);
//
//
//        //1st: create 1st task
//        TaskCommandService taskCommandService =    smartEngine.getTaskCommandService();
//        TaskQueryService taskQueryService =    smartEngine.getTaskQueryService();
//
//        String processInstanceId = processInstance.getInstanceId();
//        List<TaskInstance> taskInstanceList =     taskQueryService.find(processInstanceId);
//
//        Assert.assertNotNull(taskInstanceList);
//        assertEquals(1,taskInstanceList.size());
//
//        TaskInstance taskInstance = taskInstanceList.get(0);
//        assertEquals("theTask1", taskInstance.getName());
//
//        request.put("input", 4);
//
//        //2nd. create service task ,and then create task
//        taskCommandService.complete(taskInstance.getInstanceId(), request);
//
//
//
//        taskInstanceList =     taskQueryService.find(processInstanceId);
//
//        Assert.assertNotNull(taskInstanceList);
//        assertEquals(1,taskInstanceList.size());
//
//        taskInstance = taskInstanceList.get(0);
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
//
//
//    }
 

}
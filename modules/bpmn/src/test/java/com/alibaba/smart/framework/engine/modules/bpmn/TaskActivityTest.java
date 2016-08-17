package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.service.TaskService;

public class TaskActivityTest {


//	@Test
//	public void testExclusive() throws Exception {
//	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration(); 
//	    
//		SmartEngine smartEngine = new DefaultSmartEngine();
//		smartEngine.init(processEngineConfiguration);
//
//		RepositoryService repositoryService = smartEngine
//				.getRepositoryService();
//		ProcessDefinition processDefinition = repositoryService
//				.deploy("test-servicetask-exclusive.bpmn20.xml");
//        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());
//
//		ProcessService processService = smartEngine.getProcessService();
//		Map<String, Object> request = new HashMap<>();
//		request.put("input", 2);
//		ProcessInstance processInstance = processService.start(
//				processDefinition.getId(), processDefinition.getVersion(),
//				request);
//
//		Assert.assertNotNull(processInstance);
//	}
//	
	
	@Test
    public void testUserTaskExclusive() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration(); 
        
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-usertask-exclusive.bpmn20.xml");
        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());

        ProcessService processService = smartEngine.getProcessService();
        Map<String, Object> request = new HashMap<>();
        request.put("input", 2);
        ProcessInstance processInstance = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);
        
        Assert.assertNotNull(processInstance);

        
        //1st: create 1st task
        TaskService  taskService =    smartEngine.getTaskService();
        String processInstanceId = processInstance.getInstanceId();
        List<TaskInstance>  taskInstanceList =     taskService.find(processInstanceId);
        
        Assert.assertNotNull(taskInstanceList);
        Assert.assertEquals(1,taskInstanceList.size());
        
        TaskInstance taskInstance = taskInstanceList.get(0);
        Assert.assertEquals("theTask1", taskInstance.getName());
        
        request.put("input", 4);
        
        //2nd. create service task ,and then create task
        taskService.complete(taskInstance.getInstanceId(), request);
        
      
        
        taskInstanceList =     taskService.find(processInstanceId);
        
        Assert.assertNotNull(taskInstanceList);
        Assert.assertEquals(1,taskInstanceList.size());
        
          taskInstance = taskInstanceList.get(0);
        Assert.assertEquals("theTask4", taskInstance.getName());
        
        
        taskService.complete(taskInstance.getInstanceId(), request);

        
        //
        
        processInstance =    processService.find(processInstanceId);
        Assert.assertEquals(InstanceStatus.completed, processInstance.getStatus());

        
    }
 

}
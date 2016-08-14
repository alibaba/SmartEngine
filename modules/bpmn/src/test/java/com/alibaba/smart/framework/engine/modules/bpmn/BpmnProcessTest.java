package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;

public class BpmnProcessTest {

	@Test
	public void testExclusive() throws Exception {
	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration(); 
	    
		SmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init(processEngineConfiguration);

		RepositoryService repositoryService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-exclusive.bpmn20.xml");
        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());

		ProcessService processService = smartEngine.getProcessService();
		Map<String, Object> variables = new HashMap<>();
		variables.put("input", 2);
		ProcessInstance processInstance = processService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				variables);

		Assert.assertNotNull(processInstance);
	}

    // @Test
    // public void testParallel() throws Exception {
    // DefaultSmartEngine smartEngine = new DefaultSmartEngine();
    // smartEngine.init();
    //
    // RepositoryService repositoryService = smartEngine
    // .getRepositoryService();
    // ProcessDefinition processDefinition = repositoryService
    // .deploy("test-parallel.bpmn20.xml");
    //
    // ProcessService processService = smartEngine.getProcessService();
    // Map<String, Object> variables = new HashMap<>();
    // ProcessInstance processInstance = processService.start(
    // processDefinition.getId(), processDefinition.getVersion(),
    // variables);
    // Assert.assertNotNull(processInstance);
    //
    // }
}

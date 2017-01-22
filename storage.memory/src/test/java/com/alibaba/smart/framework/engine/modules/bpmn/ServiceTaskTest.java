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

public class ServiceTaskTest {


	@Test
	public void testExclusive() throws Exception {
	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

		SmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init(processEngineConfiguration);

		RepositoryCommandService repositoryService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-servicetask-exclusive.bpmn20.xml");
        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());

		ProcessCommandService processService = smartEngine.getProcessService();
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("input", 2);
		ProcessInstance processInstance = processService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				request);

		Assert.assertNotNull(processInstance);
	}




}
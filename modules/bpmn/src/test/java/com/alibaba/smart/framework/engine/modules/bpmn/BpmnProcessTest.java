package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.artifact.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;

public class BpmnProcessTest {

	@Test
	public void testExclusive() throws Exception {
		DefaultSmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init();

		// ExtensionPointRegistry extensionPointRegistry =
		// smartEngine.getExtensionPointRegistry();
		// ProcessContainer processContainer =
		// extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

		RepositoryService repositoryService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-exclusive.bpmn20.xml");

		// PvmProcess process = processContainer.get("test-exclusive", "1.0.0");
		// Assert.assertNotNull(process);

		ProcessService processService = smartEngine.getProcessService();
		Map<String, Object> variables = new HashMap<>();
		variables.put("input", 2);
		ProcessInstance processInstance = processService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				variables);
		Assert.assertNotNull(processInstance);
	}

	@Test
	public void testParallel() throws Exception {
		DefaultSmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init();

		RepositoryService repositoryService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-parallel.bpmn20.xml");

		ProcessService processManager = smartEngine.getProcessService();
		Map<String, Object> variables = new HashMap<>();
		ProcessInstance processInstance = processManager.start(
				processDefinition.getId(), processDefinition.getVersion(),
				variables);
		Assert.assertNotNull(processInstance);

	}
}

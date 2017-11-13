package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class PureServiceTaskTest {

    @After
    public void before(){
        ArrayListServiceTaskDelegation.getArrayList().clear();
    }

	@Test
	public void testExclusive() throws Exception {
	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

		SmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init(processEngineConfiguration);

		RepositoryCommandService repositoryService = smartEngine
				.getRepositoryCommandService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-servicetask-pure.bpmn20.xml");
        Assert.assertEquals(5, processDefinition.getProcess().getElements().size());

		ProcessCommandService processService = smartEngine.getProcessCommandService();
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("input", 2);

		ProcessInstance processInstance = processService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				request);

		Assert.assertNotNull(processInstance);
        List<String> arrayList = 	ArrayListServiceTaskDelegation.getArrayList();
        Assert.assertEquals(1,arrayList.size());
        Assert.assertEquals("2",arrayList.get(0));

    }


    @After
    public void tearDown(){
        ArrayListServiceTaskDelegation.getArrayList().clear();
    }


}
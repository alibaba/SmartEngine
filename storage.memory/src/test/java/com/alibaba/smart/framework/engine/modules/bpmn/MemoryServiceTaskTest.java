package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration("/spring/spring.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Service
public class MemoryServiceTaskTest {

    @After
    public void before(){
        ArrayListServiceTaskDelegation.getArrayList().clear();
    }

    @Autowired
    private SmartEngine smartEngine;

    @Test
	public void testExclusive() throws Exception {



		ProcessCommandService processService = smartEngine.getProcessCommandService();
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("input", 2);

		ProcessInstance processInstance = processService.start(
				"MemoryServiceTaskTest", "1.0.0",
				request);

        request.get("result");

		Assert.assertNotNull(processInstance);
        List<String> arrayList = 	ArrayListServiceTaskDelegation.getArrayList();
        Assert.assertEquals(3,arrayList.size());
        Assert.assertEquals("2",arrayList.get(0));
        Assert.assertEquals("2",arrayList.get(1));
        Assert.assertEquals("2",arrayList.get(2));

        List<ActivityInstance>  activityInstances =   processInstance.getActivityInstances();
        Assert.assertEquals("theStart",activityInstances.get(0).getProcessDefinitionActivityId());
        Assert.assertEquals("exclusiveGw1",activityInstances.get(1).getProcessDefinitionActivityId());

        Assert.assertEquals("theTask2",activityInstances.get(2).getProcessDefinitionActivityId());
        Assert.assertEquals("selectTask",activityInstances.get(3).getProcessDefinitionActivityId());
        Assert.assertEquals("exclusiveGw2",activityInstances.get(4).getProcessDefinitionActivityId());

        Assert.assertEquals("theTask6",activityInstances.get(5).getProcessDefinitionActivityId());
        Assert.assertEquals("theEnd",activityInstances.get(6).getProcessDefinitionActivityId());

    }

    @After
    public void tearDown(){
        ArrayListServiceTaskDelegation.getArrayList().clear();
    }



}
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
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import com.google.common.collect.Lists;

public class BpmnProcessTest {


	@Test
	public void testExclusive() throws Exception {
	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration(); 
	    
		SmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init(processEngineConfiguration);

		RepositoryService repositoryService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.deploy("test-servicetask-exclusive.bpmn20.xml");
        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());

		ProcessService processService = smartEngine.getProcessService();
		Map<String, Object> request = new HashMap<>();
		request.put("input", 2);
		ProcessInstance processInstance = processService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				request);

		Assert.assertNotNull(processInstance);
	}

    @Test
    public void testParallel() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-parallel.bpmn20.xml");

        ProcessService processService = smartEngine.getProcessService();
        ProcessInstance processInstance = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                null);

        processService.start(processDefinition.getId(), processDefinition.getVersion(),
                null);

        Assert.assertNotNull(processInstance);


    }



    @Test
    public void testSelectRun() {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-parallel.bpmn20.xml");


        ProcessService processService = smartEngine.getProcessService();

        ProcessInstance start = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                null);



        ProcessInstance select = processService.run(processDefinition,
                start.getInstanceId(),"theTask2",false);

        Assert.assertNotNull(start);
        Assert.assertNotNull(select);


    }



    @Test
    public void testToDataBase() {

        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-parallel.bpmn20.xml");


        ProcessService processService = smartEngine.getProcessService();

        ProcessInstance start = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                null);

        String result = processService.toDatabase(start.getInstanceId()).getExecutionsData();

        System.out.println(result);

        List<String> all = Lists.newArrayList();
        String[] groups = result.split(EngineConstant.REG_SEP_G);
        for (String group : groups) {
            String[] items = group.split(EngineConstant.REG_SEP_S);
            all.addAll(Lists.newArrayList(items));
        }

        all.stream().forEach(System.out::println);



    }

    @Test
    public void testToModle() {


        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-parallel.bpmn20.xml");


        ProcessService processService = smartEngine.getProcessService();

        ProcessInstance start = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                null);

        String result = processService.toDatabase(start.getInstanceId()).getExecutionsData();

        System.out.println(result);

        List<String> all = Lists.newArrayList();
        String[] groups = result.split(EngineConstant.REG_SEP_G);
        for (String group : groups) {
            String[] items = group.split(EngineConstant.REG_SEP_S);
            all.addAll(Lists.newArrayList(items));
        }

        all.stream().forEach(System.out::println);

        processService.clear(start.getInstanceId());

        Assert.assertNull(processService.find(start.getInstanceId()));


        EngineParam param  = EngineParam.of(start.getInstanceId(),processDefinition.getId(),processDefinition.getVersion(),result);

        processService.recovery(param);

        Assert.assertNotNull(processService.find(start.getInstanceId()));





    }




}
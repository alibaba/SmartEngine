package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class BpmnProcessTest {


	@Test
	public void testExclusive() throws Exception {
	    ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration(); 
	    
		SmartEngine smartEngine = new DefaultSmartEngine();

		//初始化流程引擎
		smartEngine.init(processEngineConfiguration);

		//部署流程定义
		RepositoryCommandService repositoryCommandService = smartEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryCommandService
				.deploy("test-servicetask-exclusive.bpmn20.xml");

		//断言
        Assert.assertEquals(25, processDefinition.getProcess().getElements().size());


		ProcessCommandService processCommandService = smartEngine.getProcessService();
		Map<String, Object> request = new HashMap<>();
		request.put("input", 2);

		//启动流程实例
		ProcessInstance processInstance = processCommandService.start(
				processDefinition.getId(), processDefinition.getVersion(),
				request);


		//断言
		Assert.assertNotNull(processInstance);
//		Assert.assertTrue(processInstance.get);

	}
//
//    @Test
//    public void testParallel() throws Exception {
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-parallel.bpmn20.xml");
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//        Map<String,Object> context = Maps.newHashMap();
//        context.put("1","1");
//
//        ProcessInstance instance = processService.start(processDefinition.getId(), processDefinition.getVersion(), context);
//
//
//
//
//
//    }
//
//
//
//    @Test
//    public void testSelectRun() {
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-parallel.bpmn20.xml");
//
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//
//        Map<String,Object> request = Maps.newHashMap();
//        request.put("1","1");
//        ProcessInstance start = processService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                request);
//
//
//
//        request.put("2","2");
//        ProcessInstance select = processService.run(processDefinition,
//                start.getInstanceId(),"theTask2",false,request);
//
//        request.put("event","createOrder");
//        ProcessInstance select2 = processService.run(processDefinition,
//                start.getInstanceId(),"theTask1",false,request);
//
//        Assert.assertNotNull(start);
//        Assert.assertNotNull(select);
//
//
//    }
//
//
//
//    @Test
//    public void testEvent() {
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-parallel.bpmn20.xml");
//
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//
//        Map<String,Object> request = Maps.newHashMap();
//        request.put("1","1");
//        ProcessInstance start = processService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                request);
//
//
//
//
//        request.put("event","createOrder");
//        ProcessInstance select2 = processService.run(processDefinition,
//                start.getInstanceId(),"theTask1",false,request);
//
//        Assert.assertNotNull(start);
//        Assert.assertNotNull(select2);
//
//
//    }
//
//
//
//    @Test
//    public void testToDataBase() {
//
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-parallel.bpmn20.xml");
//
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//
//        ProcessInstance start = processService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                null);
//
//
//
//
//    }
//
//    @Test
//    public void testToModule() {
//
//
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-parallel.bpmn20.xml");
//
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//
//        ProcessInstance start = processService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                null);
//
//
//
//
//
//
//
//    }
//
//
//    @Test
//    public void testEventsParse() throws Exception {
//        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
//        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//
//        RepositoryCommandService repositoryService = smartEngine
//                .getRepositoryService();
//        ProcessDefinition processDefinition = repositoryService
//                .deploy("test-demo.bpmn20.xml");
//
//        ProcessCommandService processService = smartEngine.getProcessService();
//        ProcessInstance processInstance = processService.start(
//                processDefinition.getId(), processDefinition.getVersion(),
//                null);
//        Map<String,Object> context = Maps.newHashMap();
//        context.put("1","1");
//
//        processService.start(processDefinition.getId(), processDefinition.getVersion(),
//                context);
//
//
//
//
//
//        Assert.assertNotNull(processInstance);
//
//
//    }




}
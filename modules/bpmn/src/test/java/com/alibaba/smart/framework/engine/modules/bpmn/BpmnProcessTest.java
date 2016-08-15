package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.ActivityParam;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.param.ExecutionParam;
import com.alibaba.smart.framework.engine.param.ProcessParam;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.alibaba.smart.framework.engine.util.EngineConstant;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BpmnProcessTest {


	@Test
	public void testExclusive() throws Exception {
		SmartEngine smartEngine = new DefaultSmartEngine();
		smartEngine.init();

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

    @Test
    public void testParallel() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();

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
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();
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


        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();
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


        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();
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


        EngineParam param  = EngineParam.of(start.getInstanceId(),result);

        processService.recovery(param);

        Assert.assertNotNull(processService.find(start.getInstanceId()));





    }


    public static void main(String[] args) {
        String test = "535467e4a879402fb507859dd14736ba|theTask3|;849ebeaa069c4aaba89ecdd3e76247fb|theTask2|;";

        List<String> all = Lists.newArrayList();
        String[] groups = test.split(EngineConstant.REG_SEP_G);
        for (String group : groups) {
            String[] items = group.split(EngineConstant.REG_SEP_S);
            all.addAll(Lists.newArrayList(items));
        }


        all.stream().forEach(System.out::println);
    }

}

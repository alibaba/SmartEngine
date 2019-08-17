package com.alibaba.smart.framework.engine.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiValueAndEventListenerTest {

    public static List<String> trace=new ArrayList<String>();


    @Test
    public void testDemo() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryService
            .deploy("MultiValueAndEventListenerTest.bpmn.xml");
        Assert.assertEquals(7,processDefinition.getProcess().getElements().size());

        ProcessCommandService processService = smartEngine.getProcessCommandService();

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("hello", "world");

        ProcessInstance processInstance = processService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);


        Assert.assertEquals(MultiValueAndEventListenerDelegation.getCounter().longValue(),1L);

        Assert.assertEquals(MultiValueAndEventListenerTest.trace.get(0),"world");
        Assert.assertEquals(MultiValueAndEventListenerTest.trace.get(1),"world");


        PersisterSession.create().putProcessInstance(processInstance);

        List<ExecutionInstance> executionInstanceList =smartEngine.getExecutionQueryService().findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertEquals("receive", firstExecutionInstance.getProcessDefinitionActivityId());

        request = new HashMap<String, Object>();
        request.put("hello", "world1");

        ProcessInstance  processInstance1 = smartEngine.getExecutionCommandService().signal(firstExecutionInstance.getInstanceId(), request);

        Assert.assertEquals(MultiValueAndEventListenerTest.trace.get(2),"world1");
        Assert.assertEquals(MultiValueAndEventListenerTest.trace.get(3),"world1");

        Assert.assertTrue(processInstance1.getStatus().equals(InstanceStatus.completed));




    }

    @Before
    public void before(){
        PersisterSession.create();
    }

    @After
    public void after(){
        PersisterSession.destroySession();
    }




}

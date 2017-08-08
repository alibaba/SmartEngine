package com.alibaba.smart.framework.engine.modules.smart;

import java.util.ArrayList;
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ProcessTest {

    public static List<String> trace;

    @Before
    public void before(){
        trace=new ArrayList<String>();
    }

    @Test
    public void testDemo() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryService
            .deploy("demo/process-demo.bpmn20.xml");
        Assert.assertEquals(7,processDefinition.getProcess().getElements().size());

        ProcessCommandService processService = smartEngine.getProcessCommandService();

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("text", "start");

        ProcessInstance processInstance = processService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);
        Assert.assertNotNull(processInstance);

        Assert.assertEquals(trace.get(0),"start");
        Assert.assertEquals(trace.get(1),"Listener: Create task");
        Assert.assertEquals(trace.get(2),"Create task");
        Assert.assertEquals(trace.get(3),"Listener: Create task");

        Assert.assertEquals(trace.get(4),"Listener: Pay task");
        Assert.assertEquals(trace.get(5),"Pay task");
        Assert.assertEquals(trace.get(6),"Listener: Pay task");
        Assert.assertEquals(trace.get(7),"Listener: Pay task");

    }

}

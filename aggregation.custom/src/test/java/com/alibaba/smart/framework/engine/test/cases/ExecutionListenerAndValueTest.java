package com.alibaba.smart.framework.engine.test.cases;

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
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ExecutionListenerAndValueTest extends BaseTestCase  {

    public static List<String> trace = new ArrayList<String>();

    @Test
    public void testDemo() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("execution_listener_and_value_test.bpmn20.xml");
        Assert.assertEquals(7, processDefinition.getProcess().getElements().size());

        ProcessCommandService processService = smartEngine.getProcessCommandService();

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("text", "start");

        ProcessInstance processInstance = processService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);
        Assert.assertNotNull(processInstance);

        Assert.assertEquals(trace.get(0), "start");
        Assert.assertEquals(trace.get(1), "Listener: start");
        Assert.assertEquals(trace.get(2), "Listener: start");
        Assert.assertEquals(trace.get(3), "start");

    }


}

package com.alibaba.smart.framework.engine.test.cases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ExecutionListenerAndValueTest extends CustomBaseTestCase {

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
        Assert.assertEquals(processInstance.getStatus(), InstanceStatus.running);


        Assert.assertEquals(4,trace.size());
        Assert.assertEquals("start",trace.get(0));
        Assert.assertEquals("Listener: start",trace.get(1));
        Assert.assertEquals("Listener: Create task",trace.get(2));
        Assert.assertEquals("Pay task",trace.get(3));


    }


}

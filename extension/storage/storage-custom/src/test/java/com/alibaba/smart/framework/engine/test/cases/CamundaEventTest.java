package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CamundaEventTest extends CustomBaseTestCase {

    public static List<String> container = new ArrayList<String>();


    @Test
    public void test() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("camunda-event-test.bpmn.xml").getFirstProcessDefinition();
        assertEquals(7, processDefinition.getBaseElementList().size());

        Map<String, Object>  request = new HashMap();
        request.put("a","2");

        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion(),request);


        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());

        System.out.println(container);

        Assert.assertTrue(container.size() ==5);

        Assert.assertTrue(container.get(0).equals("ProcessStartListener"));
        Assert.assertTrue(container.get(1).equals("ActivityStartListener"));
        Assert.assertTrue(container.get(2).equals("ActivityEndListener"));
        Assert.assertTrue(container.get(3).equals("TakeEventListener"));
        Assert.assertTrue(container.get(4).equals("ProcessEndListener"));




    }

}
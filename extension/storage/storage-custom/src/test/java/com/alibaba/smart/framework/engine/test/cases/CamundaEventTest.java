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
        assertEquals(8, processDefinition.getBaseElementList().size());

        Map<String, Object>  request = new HashMap();
        request.put("a","2");

        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion(),request);


        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());



        Assert.assertEquals(1,request.size());

        Assert.assertEquals("yin",request.get("request"));
        Assert.assertEquals("yin",request.get("yin"));
        Assert.assertEquals("one",request.get("one"));



    }

}
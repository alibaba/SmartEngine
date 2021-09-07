package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExclusiveWithoutGatewayTest extends CustomBaseTestCase {

    @Test
    public void test() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("exclusive-without-gateway.bpmn.xml").getFirstProcessDefinition();
        assertEquals(10, processDefinition.getBaseElementList().size());

        Map<String, Object>  request = new HashMap();
        request.put("request","yin");

        ProcessInstance processInstance =  processCommandService.start(processDefinition.getId(),processDefinition.getVersion(),request);


        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());


        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());

        Assert.assertEquals(3,request.size());

        Assert.assertEquals("yin",request.get("request"));
        Assert.assertEquals("yin",request.get("yin"));
        Assert.assertEquals("one",request.get("one"));



    }

}
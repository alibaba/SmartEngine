package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SmartPropertiesTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("smartPropertiesTest.bpmn.xml").getFirstProcessDefinition();
        assertEquals(10, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();
        request.put("input", "right");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request,response
        );
        Assert.assertNotNull(processInstance);
        Assert.assertTrue(processInstance.getStatus().equals(InstanceStatus.completed));

    }

}
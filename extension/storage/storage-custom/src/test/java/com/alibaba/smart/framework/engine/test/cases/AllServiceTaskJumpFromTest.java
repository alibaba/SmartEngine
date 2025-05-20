package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllServiceTaskJumpFromTest extends CustomBaseTestCase {

    @Test
    public void test() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("all-simple-servicetask.bpmn.xml").getFirstProcessDefinition();
        assertEquals(9, processDefinition.getBaseElementList().size());

        ProcessInstance processInstance =  executionCommandService.jumpTo("1",processDefinition.getId(),
                processDefinition.getVersion(),InstanceStatus.running,"serviceTask1",null);


        processInstance =   executionCommandService.jumpFrom(processInstance,"serviceTask1",null,null);

        Assert.assertEquals(InstanceStatus.completed,processInstance.getStatus());
    }

}
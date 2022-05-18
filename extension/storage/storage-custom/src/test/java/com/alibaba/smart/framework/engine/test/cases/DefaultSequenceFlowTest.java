package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;
import com.alibaba.smart.framework.engine.test.delegation.ExclusiveTaskDelegation;
import com.alibaba.smart.framework.engine.test.delegation.SimpleServiceTaskDelegation;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DefaultSequenceFlowTest extends CommonTestCode {

    @Override
    public void setUp() {
        super.setUp();

    }

    @Test
    public void test() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("default-sequence-flow.bpmn.xml").getFirstProcessDefinition();
        assertEquals(10, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();

        request.put("route","NoRoute");

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(), request,response
        );

        Object route = response.get("route");
        Object processDefinitionActivityId = response.get(SimpleServiceTaskDelegation.DEFAULT_SEQUENCE_FLOW);
        Assert.assertEquals("NoRoute",route);
        Assert.assertEquals("serviceTask1",processDefinitionActivityId);




        Assert.assertTrue(processInstance.getStatus().equals(InstanceStatus.completed));

    }




}
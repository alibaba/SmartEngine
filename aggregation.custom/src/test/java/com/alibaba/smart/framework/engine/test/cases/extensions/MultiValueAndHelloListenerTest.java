package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;
import com.alibaba.smart.framework.engine.test.delegation.MultiValueAndEventListenerDelegation;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiValueAndHelloListenerTest extends CustomBaseTestCase {

    public static List<String> trace = new ArrayList<String>();

    @Test
    public void testDemo() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("MultiValueAndEventListenerTest.bpmn.xml").getFirstProcessDefinition();
        Assert.assertEquals(7, processDefinition.getBaseElementList().size());


        Map<String, Object> request = new HashMap<String, Object>();
        request.put("hello", "world");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        Assert.assertEquals(MultiValueAndEventListenerDelegation.getCounter().longValue(), 1L);

        List<String> trace = MultiValueAndHelloListenerTest.trace;
        Assert.assertEquals(3, trace.size());

        Assert.assertEquals(trace.get(0), "world");
        Assert.assertEquals(trace.get(1), "world");
        Assert.assertEquals(trace.get(2), "world");

        PersisterSession.currentSession().putProcessInstance(processInstance);

        List<ExecutionInstance> executionInstanceList = smartEngine.getExecutionQueryService().findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertEquals("receive", firstExecutionInstance.getProcessDefinitionActivityId());

        request = new HashMap<String, Object>();
        request.put("hello", "world1");

        ProcessInstance processInstance1 = smartEngine.getExecutionCommandService().signal(
            firstExecutionInstance.getInstanceId(), request);
        Assert.assertEquals(4, trace.size());

        Assert.assertEquals(trace.get(3), "world1");

        Assert.assertTrue(processInstance1.getStatus().equals(InstanceStatus.completed));

    }


}

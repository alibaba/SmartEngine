package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceTaskParallelGatewayTest extends CustomBaseTestCase {
    private long orderId = 123456L;


    @Test
    public void testParallelGateway() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("test-servicetask-parallel-gateway.bpmn20.xml");
        assertEquals(16, processDefinition.getProcess().getElements().size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        persisteAndUpdateThreadLocal(orderId, processInstance);

        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());

        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);

        assertTrue(("theTask3".equals(firstExecutionInstance.getProcessDefinitionActivityId())));

        processInstance = persisteAndUpdateThreadLocal(orderId, processInstance);

        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }

    private ProcessInstance persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {
        String serializedProcessInstance = InstanceSerializerFacade.serialize(processInstance);
        processInstance = InstanceSerializerFacade.deserializeAll(serializedProcessInstance);

        PersisterSession.currentSession().putProcessInstance(processInstance);
        return processInstance;
    }

}
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

public class AllServiceTaskParallelGatewayTest extends CustomBaseTestCase {
    private long orderId = 123456L;




    @Test
    public void testParallelGateway() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("test-all-servicetask-parallel-gateway.bpmn20.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

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
        assertEquals(0, executionInstanceList.size());

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
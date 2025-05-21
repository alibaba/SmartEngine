package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockProcessInstanceTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("mockProcessInstance.bpmn.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        processInstance = persisteAndUpdateThreadLocal(InstanceStatus.running, "createOrder");

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        Assert.assertEquals("createOrder", firstExecutionInstance.getProcessDefinitionActivityId());
        request.put("action", "complete");
//        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        processInstance = persisteAndUpdateThreadLocal(InstanceStatus.running, "completeOrder");

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("completeOrder".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        processInstance = persisteAndUpdateThreadLocal(InstanceStatus.completed, "endEvent");

        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }

    private ProcessInstance persisteAndUpdateThreadLocal(InstanceStatus instanceStatus,
                                                         String processDefinitionActivityId) {

        ProcessInstance processInstance = InstanceSerializerFacade.mockSimpleProcessInstance("Process_1",
            "1.0.0", instanceStatus, processDefinitionActivityId);
        PersisterSession.currentSession().putProcessInstance(processInstance);
        return processInstance;
    }

    private ProcessInstance persisteAndUpdateThreadLocal(String processDefinitionId, String version,
                                                         InstanceStatus instanceStatus,
                                                         String processDefinitionActivityId) {

        ProcessInstance processInstance = InstanceSerializerFacade.mockSimpleProcessInstance(processDefinitionId,
            version, instanceStatus, processDefinitionActivityId);
        PersisterSession.currentSession().putProcessInstance(processInstance);
        return processInstance;
    }

}
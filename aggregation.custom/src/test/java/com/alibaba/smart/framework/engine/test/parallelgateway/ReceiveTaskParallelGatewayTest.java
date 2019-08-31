package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReceiveTaskParallelGatewayTest {
    private long orderId = 123456L;

    @After
    public void clear() {
        PersisterSession.destroySession();
    }

    @Before
    public void init() {
        PersisterSession.create();
    }

    @Test
    public void testParallelGateway() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();

        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();

        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("test-receivetask-parallel-gateway.bpmn20.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());

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
        assertEquals(2, executionInstanceList.size());

        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        String firstActivityId = firstExecutionInstance.getProcessDefinitionActivityId();

        ExecutionInstance secondExecutionInstance = executionInstanceList.get(1);
        String secondActivityId = secondExecutionInstance.getProcessDefinitionActivityId();

        assertTrue(("theTask1".equals(firstActivityId) && "theTask2".equals(secondActivityId)) || (
            "theTask1".equals(secondActivityId) && "theTask2".equals(firstActivityId)));

        //完成两个任务节点创建,已经进入fork节点后面的数据。
        //完成其中一个节点的驱动。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        processInstance = persisteAndUpdateThreadLocal(orderId, processInstance);

        String runningActivityId = secondActivityId;

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());

        firstExecutionInstance = executionInstanceList.get(0);
        firstActivityId = firstExecutionInstance.getProcessDefinitionActivityId();

        secondExecutionInstance = executionInstanceList.get(1);
        secondActivityId = secondExecutionInstance.getProcessDefinitionActivityId();

        ExecutionInstance runningExecutionInstance;
        if (firstActivityId.equals("join")) {
            assertEquals(runningActivityId, secondActivityId);
            runningExecutionInstance = secondExecutionInstance;
        } else {
            assertTrue(runningActivityId.equals(firstActivityId) || "join".equals(secondActivityId));
            runningExecutionInstance = firstExecutionInstance;
        }

        //完成后面一个节点的驱动。
        request.put("input", 11);

        processInstance = persisteAndUpdateThreadLocal(orderId, processInstance);

        //完成两个任务节点，完成并行网关的计算。
        processInstance = executionCommandService.signal(runningExecutionInstance.getInstanceId(), request);

        processInstance = persisteAndUpdateThreadLocal(orderId, processInstance);

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("theTask3".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        processInstance = persisteAndUpdateThreadLocal(orderId, processInstance);

        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
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
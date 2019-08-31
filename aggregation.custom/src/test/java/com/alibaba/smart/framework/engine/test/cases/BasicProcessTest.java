package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;
import com.alibaba.smart.framework.engine.test.delegation.ExclusiveTaskDelegation;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicProcessTest extends BaseTestCase {

    @Test
    public void test() throws Exception {

        PersisterSession.create();

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("basic-process.bpmn.xml");
        assertEquals(16, processDefinition.getProcess().getElements().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        persisteAndUpdateThreadLocal(processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance executionInstance = executionInstanceList.get(0);
        assertEquals("receiveTask0", executionInstance.getProcessDefinitionActivityId());
        long longValue = BasicServiceTaskDelegation.getCounter().longValue();
        Assert.assertEquals(2, longValue);

        processInstance = executionCommandService.signal(executionInstance.getInstanceId());
        persisteAndUpdateThreadLocal(processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        executionInstance = executionInstanceList.get(0);
        assertEquals("receiveTask1", executionInstance.getProcessDefinitionActivityId());
        longValue = BasicServiceTaskDelegation.getCounter().longValue();
        Assert.assertEquals(3, longValue);

        request.put("route", "a");
        processInstance = executionCommandService.signal(executionInstance.getInstanceId(), request);
        persisteAndUpdateThreadLocal(processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        executionInstance = executionInstanceList.get(0);
        assertEquals("receiveTask_a", executionInstance.getProcessDefinitionActivityId());

        processInstance = executionCommandService.signal(executionInstance.getInstanceId(), request);
        persisteAndUpdateThreadLocal(processInstance);
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
        longValue = ExclusiveTaskDelegation.getCounter().longValue();
        Assert.assertEquals(101, longValue);

    }

    private void persisteAndUpdateThreadLocal(ProcessInstance processInstance) {
        PersisterSession.currentSession().putProcessInstance(processInstance);
    }

}
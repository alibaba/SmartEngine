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

public class FirstBasicProcessTest extends CustomBaseTestCase {

    @Test
    public void test() throws Exception {
        BasicServiceTaskDelegation.resetCounter();


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("first-smart-editor.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("a",2);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        persisteAndUpdateThreadLocal(processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        Assert.assertEquals(1, BasicServiceTaskDelegation.getCounter().intValue());

    }

    @Test
    public void test2() throws Exception {
        BasicServiceTaskDelegation.resetCounter();

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("first-smart-editor.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("a",-1);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        persisteAndUpdateThreadLocal(processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());

        assertEquals(InstanceStatus.running, processInstance.getStatus());
        Assert.assertEquals(0, BasicServiceTaskDelegation.getCounter().intValue());

    }

    private void persisteAndUpdateThreadLocal(ProcessInstance processInstance) {
        PersisterSession.currentSession().putProcessInstance(processInstance);
    }

}
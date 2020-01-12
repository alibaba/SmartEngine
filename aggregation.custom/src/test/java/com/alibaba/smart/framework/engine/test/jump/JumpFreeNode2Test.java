package com.alibaba.smart.framework.engine.test.jump;

import java.util.HashMap;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author shiyang.xsy
 * @date 2017/12/6
 */
public class JumpFreeNode2Test extends CustomBaseTestCase {

    protected void initProcessConfiguation() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
    }

    @Test
    public void test() {
        super.initProcessConfiguation();

        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

        repositoryCommandService
            .deploy("smart-engine/jump2.bpmn20.xml");

        //start
        ProcessInstance processInstance = smartEngine.getProcessCommandService().start(
            "jump2-process", "1.0.0", new HashMap<String, Object>()
        );

        Assert.assertNotNull(processInstance);

        String processInstanceId = processInstance.getInstanceId();
        List<ExecutionInstance> executionInstances;

        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(2, executionInstances.size());

        Assert.assertEquals(1, session.getProcessInstances().size());

        //assert task1 task2
        ExecutionInstance executionInstance1 = null, executionInstance2 = null;
        for (ExecutionInstance executionInstance : executionInstances) {
            if ("task1".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance1 = executionInstance;
            } else if ("task2".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance2 = executionInstance;
            } else {
                Assert.fail("Error!");
            }
        }
        Assert.assertNotNull(executionInstance1);
        Assert.assertTrue(executionInstance1.isActive());
        Assert.assertNotNull(executionInstance2);
        Assert.assertTrue(executionInstance2.isActive());

        //signal task1
        this.executionCommandService.signal(executionInstance1.getInstanceId(), null);

        //assert join1 task2
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        for (ExecutionInstance executionInstance : executionInstances) {
            if ("join".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance1 = executionInstance;
            } else if ("task2".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance2 = executionInstance;
            } else {
                Assert.fail("Error!");
            }
        }

        Assert.assertNotNull(executionInstance1);
        Assert.assertTrue(executionInstance1.isActive());
        Assert.assertNotNull(executionInstance2);
        Assert.assertTrue(executionInstance2.isActive());

        // jumpFrom task1
        this.executionCommandService.jumpFrom(executionInstance1.getInstanceId(), "task1", null);

        // assert task1 task2
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        for (ExecutionInstance executionInstance : executionInstances) {
            if ("task1".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance1 = executionInstance;
            } else if ("task2".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance2 = executionInstance;
            } else {
                Assert.fail("Error!");
            }
        }

        Assert.assertNotNull(executionInstance1);
        Assert.assertTrue(executionInstance1.isActive());
        Assert.assertNotNull(executionInstance2);
        Assert.assertTrue(executionInstance2.isActive());

        // signal task2
        this.executionCommandService.signal(executionInstance2.getInstanceId(), null);

        // assert task1 join2
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        for (ExecutionInstance executionInstance : executionInstances) {
            if ("task1".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance1 = executionInstance;
            } else if ("join".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance2 = executionInstance;
            } else {
                Assert.fail("Error!");
            }
        }

        Assert.assertNotNull(executionInstance1);
        Assert.assertTrue(executionInstance1.isActive());
        Assert.assertNotNull(executionInstance2);
        Assert.assertTrue(executionInstance2.isActive());

        // jump2
        this.executionCommandService.jumpFrom(executionInstance2.getInstanceId(), "task2", null);

        // assert task1 task2
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        for (ExecutionInstance executionInstance : executionInstances) {
            if ("task1".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance1 = executionInstance;
            } else if ("task2".equals(executionInstance.getProcessDefinitionActivityId())) {
                executionInstance2 = executionInstance;
            } else {
                Assert.fail("Error!");
            }
        }

        Assert.assertNotNull(executionInstance1);
        Assert.assertTrue(executionInstance1.isActive());
        Assert.assertNotNull(executionInstance2);
        Assert.assertTrue(executionInstance2.isActive());

        // signal task1 signal task2
        this.executionCommandService.signal(executionInstance1.getInstanceId(), null);
        this.executionCommandService.signal(executionInstance2.getInstanceId(), null);

        //assert task3
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(1, executionInstances.size());

        Assert.assertEquals("task3", executionInstances.get(0).getProcessDefinitionActivityId());
        this.executionCommandService.signal(executionInstances.get(0).getInstanceId(), null);

        //assert end
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(0, executionInstances.size());
    }

}
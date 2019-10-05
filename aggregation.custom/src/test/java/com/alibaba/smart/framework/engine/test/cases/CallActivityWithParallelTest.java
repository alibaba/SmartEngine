package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 05/12/2017.
 */
public class CallActivityWithParallelTest {
    private SmartEngine smartEngine;
    ExecutionQueryService executionQueryService;
    ExecutionCommandService executionCommandService;

    @Before
    public void before() {
        this.smartEngine = new DefaultSmartEngine();
        this.smartEngine.init(new DefaultProcessEngineConfiguration());
        this.executionQueryService = this.smartEngine.getExecutionQueryService();
        this.executionCommandService = this.smartEngine.getExecutionCommandService();
        this.deploy();
    }

    private void deploy() {
        RepositoryCommandService repositoryCommandService = this.smartEngine
            .getRepositoryCommandService();
        repositoryCommandService
            .deploy("parent-process-parallel.bpmn20.xml");
        repositoryCommandService
            .deploy("sub-process.bpmn20.xml");
        repositoryCommandService
            .deploy("sub-process-suspend.bpmn20.xml");
    }

    @Test
    public void test() {
        PersisterSession.create();
        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

        ProcessInstance processInstance = smartEngine.getProcessCommandService().start(
            "parent-process", "1.0.0", new HashMap<String, Object>()
        );

        Assert.assertNotNull(processInstance);

        String processInstanceId = processInstance.getInstanceId();
        List<ExecutionInstance> parentExecutionInstances;

        // ==== Assert Task 1 ====
        Assert.assertEquals(1, session.getProcessInstances().size());
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        // ==== Signal Task 1 ====
        this.executionCommandService.signal(parentExecutionInstances.get(0).getInstanceId(), null);

        // ==== Assert Parallel 1 ====
        Assert.assertEquals(2, session.getProcessInstances().size());

        // Find sub-process
        ProcessInstance subProcessInstance1 = null;
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                // Sub-process DO NOT need suspend -> sub-process.status==completed
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
                subProcessInstance1 = instance;
            }
        }
        Assert.assertNotNull(subProcessInstance1);

        // Assert: parallel_1_task & join_1
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(2, parentExecutionInstances.size());

        ExecutionInstance parallel1TaskExecutionInstance = null, join1ExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances) {
            if ("parallel_1_task".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                parallel1TaskExecutionInstance = parentExecutionInstance;
            } else if ("join_1".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                join1ExecutionInstance = parentExecutionInstance;
            } else {
                Assert.fail("CallActivity Error!");
            }
        }
        Assert.assertNotNull(parallel1TaskExecutionInstance);
        Assert.assertTrue(parallel1TaskExecutionInstance.isActive());
        Assert.assertNotNull(join1ExecutionInstance);
        Assert.assertTrue(join1ExecutionInstance.isActive());

        // ==== Signal parallel_1_task ====
        this.executionCommandService.signal(parallel1TaskExecutionInstance.getInstanceId(), null);

        // Assert : task_2
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        ExecutionInstance task2ExecutionInstance = parentExecutionInstances.get(0);
        Assert.assertNotNull(task2ExecutionInstance);
        Assert.assertEquals("task_2", task2ExecutionInstance.getProcessDefinitionActivityId());

        // ==== Signal Task 2 ====
        this.executionCommandService.signal(task2ExecutionInstance.getInstanceId(), null);

        // ==== Assert Parallel 2 ====
        Assert.assertEquals(3, session.getProcessInstances().size());

        // Find sub-process
        ProcessInstance subProcessInstance2 = null;
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (subProcessInstance1.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());

            } else if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                // Sub-process NEED suspend -> sub-process.status==running
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
                subProcessInstance2 = instance;
            }
        }
        Assert.assertNotNull(subProcessInstance2);

        // Assert parent : parallel_2_task & parallel_2_subprocess
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(2, parentExecutionInstances.size());

        ExecutionInstance parallel2TaskExecutionInstance = null, parallel2SubProcessExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances) {
            if ("parallel_2_task".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                parallel2TaskExecutionInstance = parentExecutionInstance;
            } else if ("parallel_2_subprocess".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                parallel2SubProcessExecutionInstance = parentExecutionInstance;
            } else {
                Assert.fail("CallActivity Suspend Error!");
            }
        }
        Assert.assertNotNull(parallel2TaskExecutionInstance);
        Assert.assertTrue(parallel2TaskExecutionInstance.isActive());
        Assert.assertNotNull(parallel2SubProcessExecutionInstance);
        Assert.assertTrue(parallel2SubProcessExecutionInstance.isActive());

        // Assert sub-process : task
        List<ExecutionInstance> subProcess2ExecutionInstances = this.executionQueryService.findActiveExecutionList(
            subProcessInstance2.getInstanceId());
        Assert.assertEquals(1, subProcess2ExecutionInstances.size());

        ExecutionInstance subProcess2ExecutionInstance = subProcess2ExecutionInstances.get(0);
        Assert.assertEquals("task", subProcess2ExecutionInstance.getProcessDefinitionActivityId());

        // ==== Signal parallel_2_task ====
        this.executionCommandService.signal(parallel2TaskExecutionInstance.getInstanceId(), null);

        // Assert parent : join_2 & parallel_2_subprocess
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(2, parentExecutionInstances.size());
        ExecutionInstance join2ExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances) {
            if ("join_2".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                join2ExecutionInstance = parentExecutionInstance;
            } else if ("parallel_2_subprocess".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                parallel2SubProcessExecutionInstance = parentExecutionInstance;
            } else {
                Assert.fail("CallActivity Suspend Error!");
            }
        }

        Assert.assertNotNull(join2ExecutionInstance);
        Assert.assertTrue(join2ExecutionInstance.isActive());
        Assert.assertNotNull(parallel2SubProcessExecutionInstance);
        Assert.assertTrue(parallel2SubProcessExecutionInstance.isActive());

        // ==== Signal SubProcess task ====
        this.executionCommandService.signal(subProcess2ExecutionInstance.getInstanceId(), null);

        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (subProcessInstance1.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (subProcessInstance2.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                Assert.fail("CallActivity Signal Error!");
            }
        }

        // Assert : task_3
        parentExecutionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        ExecutionInstance task3ExecutionInstance = parentExecutionInstances.get(0);
        Assert.assertNotNull(task2ExecutionInstance);
        Assert.assertEquals("task_3", task3ExecutionInstance.getProcessDefinitionActivityId());

        // ==== Signal task_3 ====
        this.executionCommandService.signal(task3ExecutionInstance.getInstanceId(), null);

        // ==== Assert End ====
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (subProcessInstance1.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (subProcessInstance2.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else {
                Assert.fail("CallActivity Signal Error!");
            }
        }
    }
}



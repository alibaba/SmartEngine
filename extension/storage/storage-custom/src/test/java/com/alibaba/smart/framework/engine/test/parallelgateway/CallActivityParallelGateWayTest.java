package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;

import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CallActivityParallelGateWayTest {
    private SmartEngine smartEngine;
    ExecutionQueryService executionQueryService;
    ExecutionCommandService executionCommandService;

    @Before
    public void before() {

        this.smartEngine = new DefaultSmartEngine();
        DefaultProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(10));

        this.smartEngine.init(processEngineConfiguration);
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
            .deploy("child-servicetask-process.bpmn20.xml");
        repositoryCommandService
            .deploy("child-receivetask-process.bpmn20.xml");
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

        String parentProcessInstanceId = processInstance.getInstanceId();

        // ==== Assert Task 1 ====
        Assert.assertEquals(1, session.getProcessInstances().size());

        List<ExecutionInstance>   parentExecutionInstances = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        // ==== Signal Task 1 ====
        this.executionCommandService.signal(parentExecutionInstances.get(0).getInstanceId(), null);

        // ==== Assert Parallel 1 ====
        Assert.assertEquals(2, session.getProcessInstances().size());

        // Find child-process
        ProcessInstance childProcessInstance1 = null;
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (parentProcessInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                //ServiceTask Sub-process: 子流程状态应该完结了，因为里面都是ServiceTask。
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
                childProcessInstance1 = instance;
                Assert.assertNotNull(childProcessInstance1);

            }
        }

        // Assert: parallel_1_task & join_1
        List<ExecutionInstance>   parentExecutionInstances1 = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(2, parentExecutionInstances1.size());

        ExecutionInstance parallelForkOneReceiveTask = null, firstJoinExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances1) {
            if ("parallel_1_task".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                parallelForkOneReceiveTask = parentExecutionInstance;
            } else if ("join_1".equals(parentExecutionInstance.getProcessDefinitionActivityId())) {
                firstJoinExecutionInstance = parentExecutionInstance;
            } else {
                Assert.fail();
            }
        }
        Assert.assertNotNull(parallelForkOneReceiveTask);
        Assert.assertTrue(parallelForkOneReceiveTask.isActive());
        Assert.assertNotNull(firstJoinExecutionInstance);
        Assert.assertTrue(firstJoinExecutionInstance.isActive());

        // ==== Signal parallel_1_task ，当完成signal调用后，此时进入第二个阶段： 进入第二个并行网关+子流程 ====
        ProcessInstance parentProcessInstance = this.executionCommandService.signal(parallelForkOneReceiveTask.getInstanceId(), null);

        // Assert : task_2
        List<ExecutionInstance>  parentExecutionInstances3 = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances3.size());

        ExecutionInstance task2ExecutionInstance = parentExecutionInstances3.get(0);
        Assert.assertNotNull(task2ExecutionInstance);
        Assert.assertEquals("task_2", task2ExecutionInstance.getProcessDefinitionActivityId());

        // ==== Signal Task 2, 重置下session ====
        PersisterSession.destroySession();
        PersisterSession.create().putProcessInstance(parentProcessInstance);
        session = PersisterSession.currentSession();

        this.executionCommandService.signal(task2ExecutionInstance.getInstanceId(), null);

        // ==== Assert Parallel 2 ====
        Assert.assertEquals(2, session.getProcessInstances().size());

        // Find sub-process
        ProcessInstance subProcessInstance2 = null;
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (parentProcessInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                // Sub-process NEED suspend -> sub-process.status==running
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
                subProcessInstance2 = instance;
            }
        }
        Assert.assertNotNull(subProcessInstance2);

        // Assert parent : parallel_2_task & parallel_2_subprocess
        List<ExecutionInstance>  parentExecutionInstances4 = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(2, parentExecutionInstances4.size());

        ExecutionInstance parallel2TaskExecutionInstance = null, parallel2SubProcessExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances4) {
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
        List<ExecutionInstance>  parentExecutionInstances5 = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(2, parentExecutionInstances5.size());
        ExecutionInstance join2ExecutionInstance = null;
        for (ExecutionInstance parentExecutionInstance : parentExecutionInstances5) {
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
            if (childProcessInstance1.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (subProcessInstance2.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (parentProcessInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
            } else {
                Assert.fail("CallActivity Signal Error!");
            }
        }

        // Assert : task_3
        List<ExecutionInstance>  parentExecutionInstances6 = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances6.size());

        ExecutionInstance task3ExecutionInstance = parentExecutionInstances6.get(0);
        Assert.assertNotNull(task2ExecutionInstance);
        Assert.assertEquals("task_3", task3ExecutionInstance.getProcessDefinitionActivityId());

        // ==== Signal task_3 ====
        this.executionCommandService.signal(task3ExecutionInstance.getInstanceId(), null);

        // ==== Assert End ====
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (childProcessInstance1.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (subProcessInstance2.getInstanceId().equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else if (parentProcessInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
            } else {
                Assert.fail("CallActivity Signal Error!");
            }
        }

        PersisterSession.destroySession();

    }
}



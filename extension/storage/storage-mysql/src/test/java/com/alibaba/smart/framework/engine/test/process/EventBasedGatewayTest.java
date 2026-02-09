package com.alibaba.smart.framework.engine.test.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventBasedGatewayTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }

    @Test
    public void testDeploy_eventBasedGatewayParsed() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("event-based-gateway-test.bpmn20.xml").getFirstProcessDefinition();
        Assert.assertNotNull(processDefinition);
        assertEquals("event-based-gateway-test", processDefinition.getId());
    }

    @Test
    public void testStart_forksToAllBranches() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("event-based-gateway-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<String, Object>();
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request);

        Assert.assertNotNull(processInstance);
        assertEquals(InstanceStatus.running, processInstance.getStatus());

        // After start, two ICE branches should each have an active ExecutionInstance
        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());

        assertEquals(2, activeExecutions.size());

        // Verify both ICE_A and ICE_B have active executions
        Optional<ExecutionInstance> iceA = activeExecutions.stream()
            .filter(e -> "ICE_A".equals(e.getProcessDefinitionActivityId()))
            .findFirst();
        Optional<ExecutionInstance> iceB = activeExecutions.stream()
            .filter(e -> "ICE_B".equals(e.getProcessDefinitionActivityId()))
            .findFirst();

        Assert.assertTrue("ICE_A should have active execution", iceA.isPresent());
        Assert.assertTrue("ICE_B should have active execution", iceB.isPresent());
    }

    @Test
    public void testSignalBranchA_cancelsBranchB() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("event-based-gateway-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<String, Object>();
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request);

        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(2, activeExecutions.size());

        // Signal ICE_A
        Optional<ExecutionInstance> iceA = activeExecutions.stream()
            .filter(e -> "ICE_A".equals(e.getProcessDefinitionActivityId()))
            .findFirst();
        Assert.assertTrue(iceA.isPresent());

        processInstance = executionCommandService.signal(iceA.get().getInstanceId(), request);

        // After signaling ICE_A: ICE_B should be canceled, flow should be at receiveTaskA
        activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, activeExecutions.size());
        assertEquals("receiveTaskA", activeExecutions.get(0).getProcessDefinitionActivityId());
    }

    @Test
    public void testSignalBranchB_cancelsBranchA_processCompletes() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("event-based-gateway-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<String, Object>();
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request);

        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(2, activeExecutions.size());

        // Signal ICE_B
        Optional<ExecutionInstance> iceB = activeExecutions.stream()
            .filter(e -> "ICE_B".equals(e.getProcessDefinitionActivityId()))
            .findFirst();
        Assert.assertTrue(iceB.isPresent());

        processInstance = executionCommandService.signal(iceB.get().getInstanceId(), request);

        // After signaling ICE_B: ICE_A should be canceled, process should complete (ICE_B -> End_B)
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
    }

    @Test
    public void testSignalBranchA_thenContinuePath() {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("event-based-gateway-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> request = new HashMap<String, Object>();
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request);

        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());

        // Signal ICE_A
        Optional<ExecutionInstance> iceA = activeExecutions.stream()
            .filter(e -> "ICE_A".equals(e.getProcessDefinitionActivityId()))
            .findFirst();
        processInstance = executionCommandService.signal(iceA.get().getInstanceId(), request);

        // Now at receiveTaskA
        activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, activeExecutions.size());
        assertEquals("receiveTaskA", activeExecutions.get(0).getProcessDefinitionActivityId());

        // Signal receiveTaskA to complete the process
        processInstance = executionCommandService.signal(activeExecutions.get(0).getInstanceId(), request);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
    }
}

package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.*;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import org.h2.engine.Engine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
/**
 *  场景1: 不嵌套（3个分支，都触发）- 验证当所有条件都满足时，所有分支都会被执行
 *  场景2: 不嵌套（3个分支，都不触发）- 验证当所有条件都不满足时，默认分支会被执行
 *  场景3: 不嵌套（3个分支，触发1个）- 验证当部分条件满足时，只有满足条件的分支会被执行
 *  场景4: 不嵌套（3个分支，触发2个）- 验证当部分条件满足时，只有满足条件的分支会被执行
 *  场景5: 包容网关的后续节点分别是（all service）- 验证包容网关后接服务任务的情况
 *  场景6: 包容网关的后续节点分别是（service+receiver）- 验证包容网关后接服务任务和接收任务的情况
 *  场景7: 包容网关的后续节点分别是（service+userTask）- 验证包容网关后接服务任务和用户任务的情况
 *  场景8: 不嵌套，但是unbalanced（1个fork 2个join）- 验证非平衡结构的包容网关
 *  场景9: 并行网关嵌套（包容网关内部有2个小的包容网关）- 验证嵌套结构的包容网关
 */
public class InclusiveGatewayThreadMultiTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

        // 指定线程池，多线程fork
        CustomThreadFactory threadFactory = new CustomThreadFactory("smart-engine");
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(30, threadFactory));
    }

    // 自定义线程工厂
    static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory(String poolName) {
            namePrefix = poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            thread.setDaemon(false); // 设置为非守护线程
            return thread;
        }
    }

    @Test
    public void testScenario1_AllBranchesTriggered() {
        // 本case验证场景1：所有分支都触发
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml").getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(15, baseElementList.size());

        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，使所有分支都被触发
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证所有分支都被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId());
        
        // 验证执行实例中包含所有分支的活动
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertTrue(actualActivityIds.contains("service3"));
    }

    @Test
    public void testScenario2_NoBranchesTriggered() {
        // 本case验证场景2：所有分支都不触发，应走默认分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，使所有分支都不被触发
        request.put("condition1", false);
        request.put("condition2", false);
        request.put("condition3", false);


        // 当所有分支条件都不满足时，应该抛出EngineException异常
        EngineException exception = Assert.assertThrows(EngineException.class, () -> {
            processCommandService.start(
                    processDefinition.getId(), processDefinition.getVersion(),
                    request);
        });

        // 验证异常消息包含预期的内容
        assertTrue(exception.getMessage().contains("No Transitions matched"));
        assertTrue(exception.getMessage().contains("inclusiveFork"));


    }

    @Test
    public void testScenario3_OneBranchTriggered() {
        // 本case验证场景3：只触发一个分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，只触发一个分支
        request.put("condition1", true);
        request.put("condition2", false);
        request.put("condition3", false);

        long service1SleepTime = 400L;
        String service1ActivityId = "service1";

        long service2SleepTime = 500L;
        String service2ActivityId = "service2";

        long service3SleepTime = 300L;
        String service3ActivityId = "service3";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);
        request.put(service3ActivityId, service3SleepTime);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证只有一个分支被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertFalse(actualActivityIds.contains("service2"));
        assertFalse(actualActivityIds.contains("service3"));
    }

    @Test
    public void testScenario4_TwoBranchesTriggered() {
        // 本case验证场景4：触发两个分支
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllBranchesTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", false);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证两个分支被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertFalse(actualActivityIds.contains("service3"));
        assertFalse(actualActivityIds.contains("defaultService"));
    }

    @Test
    public void testScenario5_AllServiceTasks() {
        // 本case验证场景5：包容网关后接服务任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayAllServiceTaskTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后，正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        // 验证服务任务被执行
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("service1"));
        assertTrue(actualActivityIds.contains("service2"));
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("endService"));
    }

    @Test
    public void testScenario6_ServiceAndReceiveTasks() {
        // 本case验证场景6：包容网关后接服务任务和接收任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayServiceAndReceiveTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // 一个分支到达join，一个分支在receiveTask
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("receiveTask"));

        // 驱动receiveTask继续执行
        Optional<ExecutionInstance> receiveTask = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("receiveTask"))
                .findFirst();
        assertTrue(receiveTask.isPresent());
        
        processInstance = executionCommandService.signal(receiveTask.get().getInstanceId(), request);

        // 验证流程完成
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario7_ServiceAndUserTasks() {
        // 本case验证场景7：包容网关后接服务任务和用户任务
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayServiceAndUserTaskTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证用户任务创建
        List<TaskInstance> taskInstanceList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        assertEquals(1, taskInstanceList.size());
        assertEquals("userTask", taskInstanceList.get(0).getProcessDefinitionActivityId());

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());
        
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // 一个分支到达join，一个分支在userTask
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("userTask"));

        // 完成用户任务
        taskCommandService.complete(taskInstanceList.get(0).getInstanceId(), request);

        // 验证流程完成
        processInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario8_UnbalancedGateway() {
        // 本case验证场景8：不平衡的包容网关（1个fork 2个join）
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayUnbalancedTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发所有分支
        request.put("condition1", true);
        request.put("condition2", true);
        request.put("condition3", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        
        // 验证分支1和分支2已经到达第一个join，分支3继续执行
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("firstJoin"));
        assertTrue(actualActivityIds.contains("service3"));

        // 等待分支3执行完成
        Optional<ExecutionInstance> service3 = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("service3"))
                .findFirst();
        assertTrue(service3.isPresent());
        
        processInstance = executionCommandService.signal(service3.get().getInstanceId(), request);

        // 验证流程完成
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario9_NestedGateways() {
        // 本case验证场景9：嵌套的包容网关
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayNestedTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发主包容网关的所有分支
        request.put("mainCondition1", true);
        request.put("mainCondition2", true);
        // 设置条件变量，触发子包容网关1的部分分支
        request.put("subCondition1_1", true);
        request.put("subCondition1_2", false);
        // 设置条件变量，触发子包容网关2的部分分支
        request.put("subCondition2_1", false);
        request.put("subCondition2_2", true);

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        
        // 验证子包容网关的分支执行情况
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        assertTrue(actualActivityIds.contains("subService1_1"));
        assertFalse(actualActivityIds.contains("subService1_2"));
        assertFalse(actualActivityIds.contains("subService2_1"));
        assertTrue(actualActivityIds.contains("subService2_2"));

        // 完成所有活动的执行实例
        for (ExecutionInstance executionInstance : executionInstanceList) {
            if (executionInstance.isActive()) {
                processInstance = executionCommandService.signal(executionInstance.getInstanceId(), request);
            }
        }

        // 验证流程完成
        processInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
    }
}
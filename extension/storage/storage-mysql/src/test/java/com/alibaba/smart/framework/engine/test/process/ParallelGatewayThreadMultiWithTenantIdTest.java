package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TenantId;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
 *  场景1:不嵌套, 从fork开始,直接都进入join节点(中间节点不暂停,都是serviceTask), 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都在此等待,并且后续节点只会被执行一次)
 *  场景2:不嵌套, 从fork开始,分支1进入join节点,分支2进入receiveTask, 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都到齐了再触发,并且后续节点只会被执行一次)
 *  场景3:不嵌套, 从fork开始,分支1,分支2进入receiveTask,然后先后驱动流程到结束. 验证流程实例状态,流转轨迹状态,中间的bean执行逻辑,join逻辑生效(都到齐了再触发,并且后续节点只会被执行一次)
 *  场景4:嵌套, 主fork下3个子fork,这3个子fork分别模拟上面的场景1,2,3
 *  场景5:嵌套, 主fork下3个子fork,2个子fork先join后,然后再和最后一个子fork join.
 */
public class ParallelGatewayThreadMultiWithTenantIdTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {

        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

        //指定线程池,多线程fork
        CustomThreadFactory threadFactory = new CustomThreadFactory("smart-engine");
        processEngineConfiguration.setExecutorService( Executors.newFixedThreadPool(30, threadFactory));

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
    public void testScenario1()  {

        //本case验证场景1
        String tenantId="-1";
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/ParallelGatewayAllServiceTaskTest.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(16, baseElementList.size());

        Map<String, Object> request = new HashMap<String, Object>();

        long sleep1 = 400L;
        long sleep2 = 500L;

        String service1ActivityId = "service1";
        String service2ActivityId = "service2";
        String service3ActivityId = "service3";
        String service4ActivityId = "service4";

        request.put(service1ActivityId, sleep1);
        request.put(service2ActivityId, sleep2);
        request.put(service3ActivityId, sleep1);
        request.put(service4ActivityId, sleep2);
        request.put(service4ActivityId, sleep2);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        long start = System.currentTimeMillis();


        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);



        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        Assert.assertEquals(9,activityInstances.size());
        List<ExecutionInstance> executionInstanceList = executionQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(9, executionInstanceList.size());

        List<IdBasedElement> idBasedElements = baseElementList.stream()
                .map(a -> (IdBasedElement) a) // 强制类型转换
                .filter(a -> !(a instanceof SequenceFlow)) //移除SequenceFlow
                .collect(Collectors.toList());

        Set<String> processDefinitionActivityIds = idBasedElements.stream()
                .map(IdBasedElement::getId)// 过滤掉 IdBasedElement 类型
                .collect(Collectors.toSet()); // 转换为 Set

        // 获取 executionInstanceList 中所有 activityId 的集合
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
        assertEquals(8, actualActivityIds.size());  //有两个join,被移除了


        // 断言 actualActivityIds 包含 expectedActivityIds 中的所有值
        assertTrue(actualActivityIds.containsAll(processDefinitionActivityIds), "executionInstanceList 应该包含所有预期的 activityId");

        // 断言每个元素的 isActive() 返回 false
        for (ExecutionInstance instance : executionInstanceList) {
            assertFalse(instance.isActive(), "所有元素的 isActive() 应该返回 false");
        }

        List joinExecutionList =   executionInstanceList.stream()
                .filter(a -> (a.getProcessDefinitionActivityId()
                        .contains("join"))).collect(Collectors.toList());
        Assert.assertEquals(2,joinExecutionList.size());
    }

    @Test
    public void testScenario2() {
        // 本case验证场景2
        String tenantId="-1";
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/ParallelGatewayScenario2Test.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(16, baseElementList.size());

        Map<String, Object> request = new HashMap<String, Object>();

        long sleep1 = 400L;
        long sleep2 = 500L;

        String service1ActivityId = "service1";
        String service2ActivityId = "service2";
        String service3ActivityId = "service3";
        String receiveTask1ActivityId = "receiveTask1";

        request.put(service1ActivityId, sleep1);
        request.put(service2ActivityId, sleep2);
        request.put(receiveTask1ActivityId, sleep1);
        request.put(service3ActivityId, sleep2);
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        // 验证执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(2, executionInstanceList.size());
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        Assert.assertTrue(actualActivityIds.contains("join"));
        Assert.assertTrue(actualActivityIds.contains("receiveTask1"));

        Optional<ExecutionInstance> receiveTask1 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask1"))
                .findFirst();

         processInstance = executionCommandService.signal(receiveTask1.get().getInstanceId(),request);


        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());
    }

    @Test
    public void testScenario3() {
        // 本case验证场景3
        String tenantId="-1";
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/ParallelGatewayScenario3Test.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(16, baseElementList.size());




        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),processDefinition.getTenantId()
        );

        // 验证初始执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(2, executionInstanceList.size());
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
        Assert.assertTrue(actualActivityIds.contains("receiveTask2"));
        Assert.assertTrue(actualActivityIds.contains("receiveTask1"));

        // 驱动第一个receiveTask
        Optional<ExecutionInstance> receiveTask1 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask1"))
                .findFirst();
        processInstance = executionCommandService.signal(
                TenantId.builder().value(receiveTask1.get().getTenantId()).build(),
                receiveTask1.get().getInstanceId()
                );


        // 验证中间状态
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(2, executionInstanceList.size());
        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
        Assert.assertTrue(actualActivityIds.contains("join"));
        Assert.assertTrue(actualActivityIds.contains("receiveTask2"));


        // 驱动第二个receiveTask
        Optional<ExecutionInstance> receiveTask2 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask2"))
                .findFirst();
        processInstance = executionCommandService.signal(
                TenantId.builder().value(receiveTask2.get().getTenantId()).build(),
                receiveTask2.get().getInstanceId()
                );

        // 验证最终状态
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());

    }

    @Test
    public void testScenario4() {
        // 本case验证场景4
        String tenantId="-1";
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/ParallelGatewayScenario4Test.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(36, baseElementList.size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        // 启动流程
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request);

        // 验证初始执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(5, executionInstanceList.size());
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        // fork1已经到达 mainJoin
        //分支1 已经完成
        Assert.assertTrue(actualActivityIds.contains("mainJoin"));

        //分支2
        Assert.assertTrue(actualActivityIds.contains("receiveTask1"));
        Assert.assertTrue(actualActivityIds.contains("subJoin2"));

        //分支3
        Assert.assertTrue(actualActivityIds.contains("receiveTask2"));
        Assert.assertTrue(actualActivityIds.contains("receiveTask3"));


        Optional<ExecutionInstance> receiveTask1 = executionInstanceList.stream()
            .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask1"))
            .findFirst();
        assertTrue(receiveTask1.isPresent());



        // 驱动子fork2的receiveTask1
        processInstance = executionCommandService.signal(receiveTask1.get().getInstanceId(), request);

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(4, executionInstanceList.size());

        Optional<ExecutionInstance> receiveTask2 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask2"))
                .findFirst();

        assertTrue(receiveTask2.isPresent());

        Optional<ExecutionInstance> receiveTask3 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask3"))
                .findFirst();
        assertTrue(receiveTask3.isPresent());

        List<ExecutionInstance> mainJoin = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("mainJoin"))
                .collect(Collectors.toList());

        assertTrue(mainJoin.size() == 2);


        // fork1,fork2 都已经到达 mainJoin
        processInstance = executionCommandService.signal(receiveTask2.get().getInstanceId(), request);

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(4, executionInstanceList.size());



        receiveTask3 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("receiveTask3"))
                .findFirst();

        assertTrue(receiveTask3.isPresent());

        Optional<ExecutionInstance>  subJoin3 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("subJoin3"))
                .findFirst();

        assertTrue(subJoin3.isPresent());

        mainJoin = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("mainJoin"))
                .collect(Collectors.toList());

        assertTrue(mainJoin.size() == 2);

        processInstance = executionCommandService.signal(receiveTask3.get().getInstanceId(), request);

        // 验证最终状态
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());



    }



    @Test
    public void testScenario5() {
        // 本case验证场景5
        String tenantId = "-1";
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("database/ParallelGatewayScenario5Test.xml",tenantId).getFirstProcessDefinition();
        List<BaseElement> baseElementList = processDefinition.getBaseElementList();
        assertEquals(38, baseElementList.size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        // 启动流程
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(), request);

        // 验证初始执行轨迹
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(6, executionInstanceList.size());
        Set<String> actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());


        //分支1
        Assert.assertTrue(actualActivityIds.contains("subJoin1"));
        Assert.assertTrue(actualActivityIds.contains("service1"));

        //分支2
        Assert.assertTrue(actualActivityIds.contains("service3"));
        Assert.assertTrue(actualActivityIds.contains("subJoin2"));

        //分支3
        Assert.assertTrue(actualActivityIds.contains("service5"));
        Assert.assertTrue(actualActivityIds.contains("subJoin3"));


        Optional<ExecutionInstance> task1 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("service1"))
                .findFirst();
        assertTrue(task1.isPresent());

        //驱动分支1
        processInstance = executionCommandService.signal(task1.get().getInstanceId(), request);

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(5, executionInstanceList.size());

        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());


        //分支1
        Assert.assertTrue(actualActivityIds.contains("intermediateJoin"));

        //分支2
        Assert.assertTrue(actualActivityIds.contains("service3"));
        Assert.assertTrue(actualActivityIds.contains("subJoin2"));

        //分支3
        Assert.assertTrue(actualActivityIds.contains("service5"));
        Assert.assertTrue(actualActivityIds.contains("subJoin3"));

        Optional<ExecutionInstance> task3 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("service3"))
                .findFirst();

        assertTrue(task3.isPresent());

        //驱动分支2
        processInstance = executionCommandService.signal(task3.get().getInstanceId(), request);

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(3, executionInstanceList.size());

        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());

        //分支1,2 抵达 mainJoin
        Assert.assertTrue(actualActivityIds.contains("mainJoin"));

        //分支3
        Assert.assertTrue(actualActivityIds.contains("service5"));
        Assert.assertTrue(actualActivityIds.contains("subJoin3"));


        Optional<ExecutionInstance> task5 = executionInstanceList.stream()
                .filter(a -> a.getProcessDefinitionActivityId().equals("service5"))
                .findFirst();

        assertTrue(task5.isPresent());


        processInstance = executionCommandService.signal(task5.get().getInstanceId(), request);

        // 验证最终状态
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId(),processInstance.getTenantId());
        assertEquals(0, executionInstanceList.size());


    }









}
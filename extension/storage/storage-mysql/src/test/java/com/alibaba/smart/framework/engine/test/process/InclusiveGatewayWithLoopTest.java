package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InclusiveGatewayWithLoopTest extends DatabaseBaseTestCase {

    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());

        // 指定线程池，多线程fork
        CustomThreadFactory threadFactory = new CustomThreadFactory("smart-engine");
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(10, threadFactory));
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
    public void testInclusiveGatewayWithLoop() {
        // 本case验证包容网关中包含互斥网关形成环路的场景
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("database/InclusiveGatewayWithLoopTest.xml").getFirstProcessDefinition();
        
        Map<String, Object> request = new HashMap<>();
        // 设置条件变量，触发两个分支
        request.put("condition1", true);
        request.put("condition2", true);
        // 设置循环控制变量
        request.put("loopCount", 0);
        request.put("maxLoops", 3);

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

        // 第一次循环：驱动receiveTask继续执行
        Optional<ExecutionInstance> receiveTask = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("receiveTask"))
                .findFirst();
        assertTrue(receiveTask.isPresent());
        
        // 更新循环计数
        request.put("loopCount", 1);
        processInstance = executionCommandService.signal(receiveTask.get().getInstanceId(), request);
        
        // 验证仍然有活动的执行实例
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());
        
        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        // 一个分支在join，一个分支回到了receiveTask（循环）
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("receiveTask"));
        
        // 第二次循环：驱动receiveTask继续执行
        receiveTask = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("receiveTask"))
                .findFirst();
        assertTrue(receiveTask.isPresent());
        
        // 更新循环计数
        request.put("loopCount", 2);
        processInstance = executionCommandService.signal(receiveTask.get().getInstanceId(), request);
        
        // 验证仍然有活动的执行实例
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());
        
        actualActivityIds = executionInstanceList.stream()
                .map(ExecutionInstance::getProcessDefinitionActivityId)
                .collect(Collectors.toSet());
                
        // 一个分支在join，一个分支回到了receiveTask（循环）
        assertTrue(actualActivityIds.contains("inclusiveJoin"));
        assertTrue(actualActivityIds.contains("receiveTask"));
        
        // 第三次循环：驱动receiveTask继续执行，这次应该结束循环
        receiveTask = executionInstanceList.stream()
                .filter(e -> e.getProcessDefinitionActivityId().equals("receiveTask"))
                .findFirst();
        assertTrue(receiveTask.isPresent());
        
        // 更新循环计数，达到最大循环次数
        request.put("loopCount", 3);
        processInstance = executionCommandService.signal(receiveTask.get().getInstanceId(), request);

        // 验证流程完成
        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());
        
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());
    }
}
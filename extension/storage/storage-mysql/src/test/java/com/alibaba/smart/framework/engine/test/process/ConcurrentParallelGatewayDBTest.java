package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ConcurrentParallelGatewayDBTest extends DatabaseBaseTestCase {
    private long orderId = 123456L;


    protected void initProcessConfiguration() {
        super.initProcessConfiguration();

        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
        //指定线程池,多线程fork
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(10));
    }



    @Test
    public void testReceiveTaskParallelGateway() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("test-receivetask-parallel-gateway.bpmn20.xml").getFirstProcessDefinition();
        assertEquals(14, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        processInstance = processQueryService.findById(processInstance.getInstanceId());

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


        //完成两个任务节点，完成并行网关的计算。
        processInstance = executionCommandService.signal(runningExecutionInstance.getInstanceId(), request);


        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("theTask3".equals(firstExecutionInstance.getProcessDefinitionActivityId()));


        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }



}
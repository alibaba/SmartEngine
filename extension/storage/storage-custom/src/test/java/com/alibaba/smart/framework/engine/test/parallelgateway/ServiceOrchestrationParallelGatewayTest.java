package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ServiceOrchestrationParallelGatewayTest extends CustomBaseTestCase {



    protected void initProcessConfiguation() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
        processEngineConfiguration.setExecutorService(newFixedThreadPool(10));
        processEngineConfiguration.getOptionContainer().put(ConfigurationOption.SERVICE_ORCHESTRATION_OPTION);
    }

    @Test
    public void testParallelGateway() throws Exception {



        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("ServiceOrchestrationParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();

        long service1SleepTime = 400L;
        String service1ActivityId = "service1";

        long service2SleepTime = 500L;
        String service2ActivityId = "service2";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);

        long start = System.currentTimeMillis();


        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);


        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        Set<Entry<String, Object>> entries = request.entrySet();
        Assert.assertEquals(2,entries.size());

        ThreadExecutionResult service1 = (ThreadExecutionResult)request.get(service1ActivityId);
        ThreadExecutionResult service2 = (ThreadExecutionResult)request.get(service2ActivityId);

        Assert.assertEquals(service1SleepTime, service1.getPayload());
        Assert.assertEquals(service2SleepTime, service2.getPayload());

        Assert.assertNotEquals(service1.getThreadId(),service2.getThreadId());

        long end = System.currentTimeMillis();

        long duration = end-start;

        //简单拍个数据，用于表示该程序非串式执行的
        long maxExecutionTime = service1SleepTime + service2SleepTime - 100L;

        //System.out.println(duration);

        Assert.assertTrue(duration< maxExecutionTime);
    }

    @Test
    public void testLatchWaitTimeOut() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("ServiceOrchestrationParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();

        long service1SleepTime = 4000L;
        String service1ActivityId = "service1";

        long service2SleepTime = 5000L;
        String service2ActivityId = "service2";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);
        request.put(RequestMapSpecialKeyConstant.LATCH_WAIT_TIME_IN_MILLISECOND,200L);

        try{
            ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);
        }catch (Exception e){
            Assert.assertTrue(e.getCause() instanceof CancellationException);
        }


    }


    //dont use in production code
    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
            0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }


}
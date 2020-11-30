package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceOrchestrationParallelGatewayTest extends CustomBaseTestCase {

    protected void initProcessConfiguation() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
        processEngineConfiguration.setExecutorService(newFixedThreadPool(4));

        // 增加自定义的线程池
        Map<String, ExecutorService> poolMap = new HashMap<String, ExecutorService>();
        poolMap.put("poolA", newFixedThreadPool(4));
        processEngineConfiguration.setExecutorServiceMap(poolMap);

        processEngineConfiguration.getOptionContainer().put(ConfigurationOption.SERVICE_ORCHESTRATION_OPTION);
    }

    @Test
    public void testMultiThreadExecution() throws Exception {



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

    @Test
    public void testSequentialExecution() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("test-all-servicetask-parallel-gateway.bpmn20.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);

        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

    }


    @Test
    public void testException() throws Exception {



        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("ServiceOrchestrationExceptionParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();

        long service1SleepTime = 400L;
        String service1ActivityId = "service1";

        long service2SleepTime = 500L;
        String service2ActivityId = "service2";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);

        try{
             processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);
        }catch (Exception e){
            Throwable cause = e.getCause().getCause().getCause().getCause();
            Assert.assertTrue(cause instanceof  IllegalArgumentException);
        }





    }

    @Test
    public void testTimeoutWithAttribute() {

        // 网关设置超时200ms，task1->1000ms, task2->500ms
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ServiceOrchestrationTimeoutParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();

        try{
            ProcessInstance processInstance = processCommandService.start(processDefinition.getId(),
                    processDefinition.getVersion(), request, response);
        }catch (Exception e){
            Assert.assertTrue(response.isEmpty());
            Assert.assertTrue(e.getCause() instanceof CancellationException);
        }
    }

    @Test
    public void testSkipTimeoutWithAttribute() {

        // 网关设置超时600ms，task1->1000ms, task2->500ms。预期不抛异常仅打印CancellationException的log，返回结果中有task2的处理结果
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ServiceOrchestrationSkipTimeoutParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();

        try{
            ProcessInstance processInstance = processCommandService.start(processDefinition.getId(),
                    processDefinition.getVersion(), request, response);
        }catch (Exception e){
            Assert.assertTrue(e.getCause() instanceof CancellationException);
        }
        Assert.assertEquals(response.size(), 1);
        Assert.assertTrue(response.containsKey("taskB"));
    }




    //dont use in production code
    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
            0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }


}
package com.alibaba.smart.framework.engine.test.parallelgateway.orchestration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import com.alibaba.smart.framework.engine.util.ThreadPoolUtil;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceOrchestrationParallelGatewayTest extends CustomBaseTestCase {

    protected void initProcessConfiguration() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();

        //指定线程池,多线程fork
        processEngineConfiguration.setExecutorService( Executors.newFixedThreadPool(10));
        // 服务编排场景,必须要手动开启这个开关
        processEngineConfiguration.getOptionContainer().put(ConfigurationOption.SERVICE_ORCHESTRATION_OPTION);

        // 增加自定义的线程池,如果没必要,也可以不用设置.
        Map<String, ExecutorService> poolMap = new HashMap<String, ExecutorService>();
        poolMap.put("poolA", newFixedThreadPool(4));
        processEngineConfiguration.setExecutorServiceMap(poolMap);

    }

    @Test
    public void testMultiThreadExecution() throws Exception {



        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("ServiceOrchestrationParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

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

        long start = System.currentTimeMillis();


        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);


        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        Set<Entry<String, Object>> entries = request.entrySet();
        Assert.assertEquals(4,entries.size());

        ThreadExecutionResult service1 = (ThreadExecutionResult)request.get(service1ActivityId);
        ThreadExecutionResult service2 = (ThreadExecutionResult)request.get(service2ActivityId);

        ThreadExecutionResult service3 = (ThreadExecutionResult)request.get(service3ActivityId);
        ThreadExecutionResult service4 = (ThreadExecutionResult)request.get(service4ActivityId);

        Assert.assertEquals(sleep1, service1.getPayload());
        Assert.assertEquals(sleep2, service2.getPayload());
        Assert.assertEquals(sleep1, service3.getPayload());

        Assert.assertEquals(sleep2, service4.getPayload());

        Assert.assertNotEquals(service1.getThreadId(),service4.getThreadId());

        long end = System.currentTimeMillis();

        long duration = end-start;

        //简单拍个数据，用于表示该程序非串式执行的  . service1,2 串行, 3,4 串行; 12和34 并发.
        long maxExecutionTime = sleep1 + sleep2 + 200L;

//        System.out.println(duration);

        Assert.assertTrue(duration< maxExecutionTime);
    }

    @Test
    public void testLatchWaitTimeOut() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("ServiceOrchestrationParallelGatewayTest.xml").getFirstProcessDefinition();
        assertEquals(16, processDefinition.getBaseElementList().size());

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
            Throwable cause1 = e.getCause();
            Throwable cause2 = cause1.getCause();
            Throwable cause3 = cause2.getCause();
            Assert.assertTrue(cause3 instanceof  IllegalArgumentException);
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
    public void testRunAnyOf() {

        // 网关不设置超时，task1->1000ms, task2->500ms。预期返回task1，response中size为1
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ServiceOrchestrationParallelGatewayRunFirstTest.xml").getFirstProcessDefinition();
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

        // 耗时短的先返回，其他线程直接cancel
        Assert.assertEquals(1, response.size());
        ThreadExecutionResult result = response.values().toArray(new ThreadExecutionResult[0])[0];
        // 500ms的先返回
        Assert.assertEquals(500, result.getPayload());
    }

    @Test
    public void testRunAnyOfWithTimeout() {

        // 网关设置超时300ms，task1->1000ms, task2->500ms，超时抛异常。
        // 预期response中为空，抛TimeoutException异常。
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ServiceOrchestrationParallelGatewayRaceTimeout.xml").getFirstProcessDefinition();
        assertEquals(12, processDefinition.getBaseElementList().size());

        Map<String, Object> request = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<String, Object>();

        try{
            ProcessInstance processInstance = processCommandService.start(processDefinition.getId(),
                    processDefinition.getVersion(), request, response);
        }catch (Exception e){
            Assert.assertTrue(response.isEmpty());
            Assert.assertTrue(e.getCause() instanceof TimeoutException);
        }
    }

    @Test
    public void testRunAnyOfWithIgnoreTimeout() {

        // 网关设置超时300ms，task1->1000ms, task2->500ms，忽略超时异常。
        // 预期response中为空，仅打印log，不抛异常。
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ServiceOrchestrationParallelGatewayRaceTimeoutIgnore.xml").getFirstProcessDefinition();
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

        // 耗时短的先返回，其他线程直接cancel
        Assert.assertEquals(0, response.size());
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




    //do NOT use in production code
    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
            0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }


}
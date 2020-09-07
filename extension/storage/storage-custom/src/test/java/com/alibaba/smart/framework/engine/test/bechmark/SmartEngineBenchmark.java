/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.alibaba.smart.framework.engine.test.bechmark;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.parallelgateway.ThreadExecutionResult;

import org.junit.Assert;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;



@State(value = Scope.Benchmark)
@BenchmarkMode({Mode.Throughput,Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 60, timeUnit = TimeUnit.SECONDS)
@Threads(value = 16)
@Fork(value =1,jvmArgsAppend = { "-server","-Xms4g","-Xmx4g","-Xmn1536m","-XX:CMSInitiatingOccupancyFraction=82", "-Xss256k", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+CMSParallelRemarkEnabled","-XX:LargePageSizeInBytes=128m","-XX:+UseFastAccessorMethods","-XX:+UseCMSInitiatingOccupancyOnly","-XX:+CMSClassUnloadingEnabled" })
public class SmartEngineBenchmark {

    private static long orderId = 123456L;

    private static ProcessCommandService processCommandService;
    private static ExecutionQueryService executionQueryService;
    private static  ExecutionCommandService executionCommandService ;
    private static ProcessDefinition processDefinition;
    private static RepositoryCommandService repositoryCommandService;

    //模拟应用启动时,加载
    static {
    //1.初始化
    ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
    processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());
    processEngineConfiguration = new DefaultProcessEngineConfiguration();
    LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
    processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
    processEngineConfiguration.setExecutorService(newFixedThreadPool(10));
    processEngineConfiguration.getOptionContainer().put(ConfigurationOption.SERVICE_ORCHESTRATION_OPTION);

    SmartEngine smartEngine = new DefaultSmartEngine();
    smartEngine.init(processEngineConfiguration);


    //2.获得常用服务
      processCommandService = smartEngine.getProcessCommandService();
      executionQueryService = smartEngine.getExecutionQueryService();
      executionCommandService = smartEngine.getExecutionCommandService();


    //3. 部署流程定义
     repositoryCommandService = smartEngine
            .getRepositoryCommandService();

      repositoryCommandService
            .deploy("first-smart-editor.xml").getFirstProcessDefinition();
        repositoryCommandService
            .deploy("ServiceOrchestrationParallelGatewayTest.xml").getFirstProcessDefinition();

}

    //@Benchmark
    //public static  void basicExclusiveAndDelegationExecution(){
    //    PersisterSession.create();
    //
    //
    //    //4.启动流程实例
    //    Map<String, Object> request = new HashMap<String, Object>();
    //    request.put("a",2);
    //
    //    ProcessInstance processInstance = processCommandService.start(
    //        "process_1", "1.0.0", request
    //    );
    //
    //    persisteAndUpdateThreadLocal(orderId,processInstance);
    //
    //    List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
    //        processInstance.getInstanceId());
    //    assertEquals(0, executionInstanceList.size());
    //
    //    assertEquals(InstanceStatus.completed, processInstance.getStatus());
    //
    //
    //    PersisterSession.destroySession();
    //
    //}


    @Benchmark
    public static  void parallelGatewayForMultiThread(){
        PersisterSession.create();



        Map<String, Object> request = new HashMap<String, Object>();

        long service1SleepTime = 400L;
        String service1ActivityId = "service1";

        long service2SleepTime = 500L;
        String service2ActivityId = "service2";

        request.put(service1ActivityId, service1SleepTime);
        request.put(service2ActivityId, service2SleepTime);

        ProcessInstance processInstance = processCommandService.start(
            "parallel", "1.0.0",
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

        PersisterSession.destroySession();

    }


    private static void persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {

        // 存储到业务系统里面
        String string =  InstanceSerializerFacade.serialize(processInstance);
        //persisterStrategy.update(orderId,string);

        // 注意:在执行之前,更新下ThreadLocal。另外,在线上环境,使用完毕后需要clean 下 ThreadLocal。
        processInstance =  InstanceSerializerFacade.deserializeAll(string);
        PersisterSession.currentSession().setProcessInstance(processInstance);
    }

    //dont use in production code
    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
            0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }

    public static void main(String[] args)  throws RunnerException {
        Options options = new OptionsBuilder()
                .include(".*" + SmartEngineBenchmark.class.getSimpleName() + ".*")
                .forks(2).mode(Mode.SampleTime)
                .build();
        new Runner(options).run();
    }
}
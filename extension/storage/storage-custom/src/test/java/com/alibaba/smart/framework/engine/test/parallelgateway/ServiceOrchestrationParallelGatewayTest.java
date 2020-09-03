package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        request.put("service1", 2000L);
        request.put("service2", 1200L);
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request);


        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);

        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        Set<Entry<Long, Long>> entries = ServiceTaskOrchestrationDelegation.getMap().entrySet();
        Assert.assertEquals(2,entries.size());


    }


    //dont use in production code
    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
            0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
    }


}
package com.alibaba.smart.framework.engine.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.simulation.ProcessSimulation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleProcessTest {


    @After
    public void clear(){
        PersisterSession.destroySession();
    }


    @Test
    public void test() throws Exception {

        PersisterSession.create();

        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();


        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        ProcessSimulation  processSimulation = new ProcessSimulation(smartEngine);

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("simple-process-simulation.bpmn.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();






        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),request
        );
        Assert.assertNotNull(processInstance);
        long longValue = BasicServiceTaskDelegation.getCounter().longValue();

        Assert.assertEquals(2,longValue);


        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        //persisteAndUpdateThreadLocal(orderId, processInstance);
    }



}
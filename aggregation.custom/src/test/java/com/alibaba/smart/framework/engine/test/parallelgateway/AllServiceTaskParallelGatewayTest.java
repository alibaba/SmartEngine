package com.alibaba.smart.framework.engine.test.parallelgateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AllServiceTaskParallelGatewayTest {
    @After
    public void clear(){
        PersisterSession.destroySession();
    }

    @Before
    public void init(){
        PersisterSession.create();
    }

    private long orderId = 123456L;


    @Test
    public void testParallelGateway() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();

        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();


        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-all-servicetask-parallel-gateway.bpmn20.xml");
        assertEquals(16, processDefinition.getProcess().getElements().size());



        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);

        persisteAndUpdateThreadLocal(orderId,processInstance);

        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);


        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());



        Assert.assertNotNull(processInstance.getCompleteTime());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());


    }

    private ProcessInstance  persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {
        String serializedProcessInstance = InstanceSerializerFacade.serialize(processInstance);
        processInstance = InstanceSerializerFacade.deserializeAll(serializedProcessInstance);

        PersisterSession.currentSession().putProcessInstance(processInstance);
        return processInstance;
    }




}
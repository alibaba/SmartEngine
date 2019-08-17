package com.alibaba.smart.framework.engine.test;

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
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OfcMarketPlaceTest {


    private long orderId = 123456L;

    @After
    public void clear(){
        PersisterSession.destroySession();
    }


    @Test
    public void test() throws Exception {

        PersisterSession.create();
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());
        //processEngineConfiguration.setPersisterStrategy(new AliPayPersisterStrategy());

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("ofcMp.bpmn.xml");
        assertEquals(16, processDefinition.getProcess().getElements().size());



        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),request
        );
        Assert.assertNotNull(processInstance);

        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        persisteAndUpdateThreadLocal(orderId, processInstance);

        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertEquals("package", firstExecutionInstance.getProcessDefinitionActivityId());



        //完成预下单,将流程驱动到 下单确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("RTS".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("update_and_notify_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        request.put("action","fail");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("update_and_notify_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));


        request.put("action","pass");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
        persisteAndUpdateThreadLocal(orderId, processInstance);
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

//        PersisterSession.destroySession();

    }

    private void persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {

        PersisterSession.currentSession().putProcessInstance(processInstance);
    }


}
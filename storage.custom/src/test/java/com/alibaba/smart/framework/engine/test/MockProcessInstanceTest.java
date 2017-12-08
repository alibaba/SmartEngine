//package com.alibaba.smart.framework.engine.test;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.alibaba.smart.framework.engine.SmartEngine;
//import com.alibaba.smart.framework.engine.configuration.PersisterStrategy;
//import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
//import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
//import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
//import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
//import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
//import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
//import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
//import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
//import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;
//import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
//import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
//import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
//import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class MockProcessInstanceTest {
//
//
//    private long orderId = 123456L;
//
//    @After
//    public void clear(){
//        PersisterSession.destroySession();
//    }
//
//
//    @Test
//    public void test() throws Exception {
//
//        PersisterSession.create();
//        //1.初始化
//        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
//        //processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());
//
//        SmartEngine smartEngine = new DefaultSmartEngine();
//        smartEngine.init(processEngineConfiguration);
//
//
//        //2.获得常用服务
//        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
//        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
//        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();
//
//
//        //3. 部署流程定义
//        RepositoryCommandService repositoryCommandService = smartEngine
//                .getRepositoryCommandService();
//        ProcessDefinition processDefinition = repositoryCommandService
//                .deploy("mockProcessInstance.bpmn.xml");
//        assertEquals(12, processDefinition.getProcess().getElements().size());
//
//
//
//        //4.启动流程实例
//        Map<String, Object> request = new HashMap<String, Object>();
//
//        ProcessInstance processInstance = processCommandService.start(
//                processDefinition.getId(), processDefinition.getVersion(),request
//        );
//        Assert.assertNotNull(processInstance);
//
//        processInstance =     persisteAndUpdateThreadLocal( InstanceStatus.running, "createOrder" );
//
//
//        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
//        assertEquals(1, executionInstanceList.size());
//        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
//        Assert.assertEquals("createOrder",firstExecutionInstance.getProcessDefinitionActivityId());
//        request.put("action","complete");
//        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
//
//
//        processInstance =    persisteAndUpdateThreadLocal( InstanceStatus.running, "completeOrder" );
//
//
//        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
//        firstExecutionInstance = executionInstanceList.get(0);
//        assertEquals(1, executionInstanceList.size());
//        assertTrue("completeOrder".equals(firstExecutionInstance.getProcessDefinitionActivityId()));
//
//
//        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
//
//        processInstance =    persisteAndUpdateThreadLocal( InstanceStatus.completed, "endEvent" );
//
//        assertEquals(InstanceStatus.completed, processInstance.getStatus());
//
//
//
//    }
//
//    private ProcessInstance persisteAndUpdateThreadLocal( InstanceStatus instanceStatus, String processDefinitionActivityId ) {
//
//        ProcessInstance processInstance = InstanceSerializerFacade.mockSimpleProcessInstance("mock_trade_process_test","1.0.0",instanceStatus,processDefinitionActivityId);
//        PersisterSession.currentSession().putProcessInstance(processInstance);
//        return processInstance;
//    }
//
//    private ProcessInstance persisteAndUpdateThreadLocal(String  processDefinitionId,String version, InstanceStatus instanceStatus, String processDefinitionActivityId ) {
//
//        ProcessInstance processInstance = InstanceSerializerFacade.mockSimpleProcessInstance(processDefinitionId,version,instanceStatus,processDefinitionActivityId);
//        PersisterSession.currentSession().putProcessInstance(processInstance);
//        return processInstance;
//    }
//
//
//}
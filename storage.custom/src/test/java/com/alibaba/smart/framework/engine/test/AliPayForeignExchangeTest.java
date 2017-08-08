package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.persister.PersisterStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializer;
import com.alibaba.smart.framework.engine.persister.util.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AliPayForeignExchangeTest {

    private PersisterStrategy persisterStrategy = new AliPayPersisterStrategy();

    private long orderId = 123456L;


    @Test
    public void test() throws Exception {

        PersisterSession.create();
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());
        processEngineConfiguration.setPersisterStrategy(new AliPayPersisterStrategy());

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionInstanceQueryService executionQueryService = smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("alipay-forex.bpmn20.xml");
        assertEquals(28, processDefinition.getProcess().getElements().size());



        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction","pre_order");

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),request
        );
        Assert.assertNotNull(processInstance);

        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        persisteAndUpdateThreadLocal(orderId, processInstance);

        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        //完成预下单,将流程驱动到 下单确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("confirm_order".equals(firstExecutionInstance.getActivityId()));

        //完成下单确认,将流程驱动到等待资金到账环节。
        request.put("smartEngineAction", "go_to_pay");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("wait_money_into_account".equals(firstExecutionInstance.getActivityId()));

        //完成资金到账,将流程驱动到资金交割处理环节。
        request.put("smartEngineAction", "money_into_account");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("fund_delivery".equals(firstExecutionInstance.getActivityId()));

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("fund_delivery_ack".equals(firstExecutionInstance.getActivityId()));

        //完成流程驱动。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);


        persisteAndUpdateThreadLocal(orderId, processInstance);
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        PersisterSession.destroySession();


    }

    private void persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {

        
        PersisterSession.currentSession().putProcessInstance(processInstance);
    }


}
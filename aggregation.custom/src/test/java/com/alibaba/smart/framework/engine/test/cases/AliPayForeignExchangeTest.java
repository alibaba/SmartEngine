package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AliPayForeignExchangeTest extends CustomBaseTestCase {

    private long orderId = 123456L;

    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("alipay-forex.bpmn20.xml");
        assertEquals(28, processDefinition.getProcess().getElements().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction", "pre_order");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);

        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        persisteAndUpdateThreadLocal(orderId, processInstance);

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        //完成预下单,将流程驱动到 下单确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("confirm_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //完成下单确认,将流程驱动到等待资金到账环节。
        request.put("smartEngineAction", "go_to_pay");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("wait_money_into_account".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //完成资金到账,将流程驱动到资金交割处理环节。
        request.put("smartEngineAction", "money_into_account");
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("fund_delivery".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        persisteAndUpdateThreadLocal(orderId, processInstance);
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("fund_delivery_ack".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //完成流程驱动。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        persisteAndUpdateThreadLocal(orderId, processInstance);
        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        String seriabledProcessInstance = InstanceSerializerFacade.serialize(processInstance);
        assertNotNull(seriabledProcessInstance);

        //        PersisterSession.destroySession();

    }

    private void persisteAndUpdateThreadLocal(long orderId, ProcessInstance processInstance) {

        PersisterSession.currentSession().putProcessInstance(processInstance);
    }

}
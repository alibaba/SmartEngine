package com.alibaba.smart.framework.engine.test.demo;

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
import com.alibaba.smart.framework.engine.persister.util.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;
import com.alibaba.smart.framework.engine.test.AliPayPersisterStrategy;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:31.
 */
public class CallActivityProcessTest {

    //private PersisterStrategy persisterStrategy = new AliPayPersisterStrategy();

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
            .deploy("parent-callactivity-process.bpmn20.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());

        processDefinition = repositoryCommandService
            .deploy("callactivity-process.bpmn20.xml");
        assertEquals(7, processDefinition.getProcess().getElements().size());



        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction","pre_order");

        ProcessInstance processInstance = processCommandService.start(
            "parent-callactivity", "1.0.0",request
        );
        Assert.assertNotNull(processInstance);

        //在调用findActiveExecution和signal方法前调用此方法。当然,在实际场景下,persiste通常只需要调用一次;UpdateThreadLocal则很多场景下需要调用。
        updateThreadLocal( 333L, processEngineConfiguration);

        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());
        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("pre_order".equals(firstExecutionInstance.getActivityId()));
        //完成预下单,将流程驱动到 下单确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        //TODO 这里需要断言父子数据均是正确。

        //测试下是否符合预期
        updateThreadLocal(1033L,processEngineConfiguration);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(1, executionInstanceList.size());

        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("debit".equals(firstExecutionInstance.getActivityId()));

        //完成下单确认,将流程驱动到等待资金到账环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        updateThreadLocal( 1033L, processEngineConfiguration);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("checkDebitResult".equals(firstExecutionInstance.getActivityId()));

        //完成资金到账,将流程驱动到资金交割处理环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);

        //测试下是否符合预期
        updateThreadLocal( 333L, processEngineConfiguration);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("end_order".equals(firstExecutionInstance.getActivityId()));

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        updateThreadLocal(333L,  processEngineConfiguration);
        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        PersisterSession.destroySession();


    }

    //BE AWARE:如果在主流程中,则需要设置父流程实例id,否则需要设置子流程实例id。
    private void updateThreadLocal(Long pid, ProcessEngineConfiguration processEngineConfiguration) {


        ProcessInstance processInstance = processEngineConfiguration.getPersisterStrategy().getProcessInstance(pid);

        PersisterSession.currentSession().setProcessInstance(processInstance);
    }
}

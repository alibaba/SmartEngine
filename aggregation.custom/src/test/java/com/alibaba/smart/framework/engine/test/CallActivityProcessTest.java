package com.alibaba.smart.framework.engine.test;

import java.util.Collection;
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
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;

import org.junit.After;
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
        ProcessQueryService processQueryService = smartEngine.getProcessQueryService();

        ExecutionQueryService executionQueryService = smartEngine.getExecutionQueryService();
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
        ProcessInstance subProcessInstance=null;
        Assert.assertNotNull(processInstance);

        String processInstanceId = processInstance.getInstanceId();
        String subProcessInstanceId = null;

        List<ExecutionInstance> executionInstanceList;
        ExecutionInstance firstExecutionInstance;

        List<ExecutionInstance> subExecutionInstanceList;
        ExecutionInstance firstSubExecutionInstance;

        //只有主流程一个实例
        Collection<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(1, processInstanceIds.size());

        executionInstanceList =executionQueryService.findActiveExecutionList(processInstanceId);
        assertEquals(1, executionInstanceList.size());
        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("pre_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));
        //完成预下单,将流程驱动到 下单确认环节。
        executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        //因为执行到了callActivity节点,有主流程和子流程两个实例
        processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(2, processInstanceIds.size());
        for (String instanceId : processInstanceIds) {
            if (!processInstanceId.equals(instanceId)) {
                subProcessInstanceId = instanceId;
            }
        }

        //TODO 这里需要断言父子数据均是正确。

        // 主流程在callActivity节点
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstanceId);
        assertEquals(1, executionInstanceList.size());
        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        // 子流程在debit节点
        subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(1, subExecutionInstanceList.size());
        firstSubExecutionInstance = subExecutionInstanceList.get(0);
        assertTrue("debit".equals(firstSubExecutionInstance.getProcessDefinitionActivityId()));

        // TODO ettear 如果这时执行主流程应该异常
        //processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);


        //完成下单确认,将流程驱动到等待资金到账环节。
        executionCommandService.signal(firstSubExecutionInstance.getInstanceId(), request);

        // 主流程还是在callActivity节点
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstanceId);
        assertEquals(1, executionInstanceList.size());
        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //子流程在checkDebitResult节点
        subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(1, subExecutionInstanceList.size());
        firstSubExecutionInstance = subExecutionInstanceList.get(0);
        assertTrue("checkDebitResult".equals(firstSubExecutionInstance.getProcessDefinitionActivityId()));

        //完成资金到账,将流程驱动到资金交割处理环节。
        executionCommandService.signal(firstSubExecutionInstance.getInstanceId(), request);

        //主流程结束callActivity节点，在end_order节点
        executionInstanceList = executionQueryService.findActiveExecutionList(processInstanceId);
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("end_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //子流程结束
        subProcessInstance= processQueryService.findById(subProcessInstanceId);
        Assert.assertEquals(InstanceStatus.completed,subProcessInstance.getStatus());
        subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(0, subExecutionInstanceList.size());

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, processInstance.getStatus());

//        PersisterSession.destroySession();


    }
}

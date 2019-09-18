package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.param.query.PaginateQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParaGatewayUserTaskProcessTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguation() {
        super.initProcessConfiguation();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DefaultLockStrategy());
    }

    @Test
    public void passed() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("parallel-gateway-usertask-process.bpmn.xml");


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start("parallel-gateway-usertask-process","2.0.0");
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(2,submitTaskInstanceList.size());
        TaskInstance first = submitTaskInstanceList.get(0);
        TaskInstance second = submitTaskInstanceList.get(1);

        String firstProcessDefinitionActivityId = first.getProcessDefinitionActivityId();
        String secondProcessDefinitionActivityId = second.getProcessDefinitionActivityId();

        Assert.assertTrue( ("processPayment".equals(firstProcessDefinitionActivityId) &&   "processDelivery".equals(secondProcessDefinitionActivityId)) ||
            ("processPayment".equals(secondProcessDefinitionActivityId) &&   "processDelivery".equals(firstProcessDefinitionActivityId)));


        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();

        //完成支付或者发货
        TaskInstance firstTaskInstance = submitTaskInstanceList.get(0);
        taskCommandService.complete(firstTaskInstance.getInstanceId(),submitFormRequest);

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1,submitTaskInstanceList.size());

        TaskInstance secondTaskInstance = submitTaskInstanceList.get(0);
        taskCommandService.complete(secondTaskInstance.getInstanceId(),submitFormRequest);


        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        List<ExecutionInstance> executionInstanceList =  executionQueryService.findActiveExecutionList(processInstance.getInstanceId());

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance thirdTaskInstance = submitTaskInstanceList.get(0);

        taskCommandService.complete(thirdTaskInstance.getInstanceId(),submitFormRequest);


        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());



    }


}
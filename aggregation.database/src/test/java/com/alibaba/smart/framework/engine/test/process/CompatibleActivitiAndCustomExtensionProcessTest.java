package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.instance.util.IOUtil;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
public class CompatibleActivitiAndCustomExtensionProcessTest {


    public static List<Object> trace=new ArrayList<Object>();

    @Test
    public void testMultiInstance() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
        ProcessQueryService processQueryService = smartEngine.getProcessQueryService();
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();
        ExecutionQueryService executionQueryService =  smartEngine.getExecutionQueryService();
        VariableQueryService variableQueryService = smartEngine.getVariableQueryService();
        ExecutionCommandService executionCommandService =  smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String(
            "compatible-activiti-and-custom-extension-process-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("type");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);


        //4.启动流程实例

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"type");
        request.put("processVariable","processVariableValue");


        ProcessInstance processInstance = processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
              ,request  );
        Assert.assertNotNull(processInstance);
        Assert.assertEquals("type",processInstance.getProcessDefinitionType());


        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),"5");
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);



        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        //submitFormRequest.put("assigneeUserId","1");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG,FullMultiInstanceTest.AGREE);
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"5");
        submitFormRequest.put("text","123");

        //submitFormRequest.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"type");



        //6.流程流转:处理 submitTask,完成任务申请.
        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);

        TaskInstance taskInstance =  taskQueryService.findOne(submitTaskInstance.getInstanceId());


        List<TaskInstance>  assertedTaskInstanceList=   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),"3");
        Assert.assertEquals(1,assertedTaskInstanceList.size());
        Assert.assertEquals("userTask1",assertedTaskInstanceList.get(0).getProcessDefinitionActivityId());


        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        Assert.assertEquals(2,activeExecutions.size());
        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(2,submitTaskInstanceList.size());
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        Assert.assertEquals(1,activeExecutions.size());
        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1,submitTaskInstanceList.size());
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        //至此,上面3个节点都应该被完成了. 此时会进入新的会签节点.
        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        Assert.assertEquals("userTask2",submitTaskInstanceList.get(0).getProcessDefinitionActivityId());

        Order order = new Order();
        order.setYzje(101L);
        submitFormRequest.put("order",order);
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        //根据网关选择,此时会进入新的会签节点.
        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        Assert.assertEquals("userTask3",submitTaskInstanceList.get(0).getProcessDefinitionActivityId());

        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        //此时会进入新的会签节点.
        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        Assert.assertEquals("UserTask_0ixdrmt",submitTaskInstanceList.get(0).getProcessDefinitionActivityId());

        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);


        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());
    }


    @Test
    public void testCouterSign_Fail_All_modle() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
        ProcessQueryService processQueryService = smartEngine.getProcessQueryService();
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();
        ExecutionQueryService executionQueryService =  smartEngine.getExecutionQueryService();
        VariableQueryService variableQueryService = smartEngine.getVariableQueryService();
        ExecutionCommandService executionCommandService =  smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String(
            "compatible-activiti-and-custom-extension-process-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("type");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);


        //4.启动流程实例

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"type");
        request.put("processVariable","processVariableValue");


        ProcessInstance processInstance = processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
            ,request  );
        Assert.assertNotNull(processInstance);
        Assert.assertEquals("type",processInstance.getProcessDefinitionType());


        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);



        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        //submitFormRequest.put("assigneeUserId","1");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG,FullMultiInstanceTest.DISAGREE);
        submitFormRequest.put("text","123");

        //submitFormRequest.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"type");

        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);



        //6.流程流转:处理 submitTask,完成任务申请.

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.aborted,finalProcessInstance.getStatus());
    }





}
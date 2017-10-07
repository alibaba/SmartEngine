package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpeicalKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
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
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
public class FullMultiInstanceTest {


    @Test
    public void testMultiInstance() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeService(new DefaultTaskAssigneeService());
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        DeploymentCommandService deploymentCommandService = smartEngine.getDeploymentCommandService();
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
        ProcessQueryService processQueryService = smartEngine.getProcessQueryService();
        DeploymentQueryService deploymentQueryService =  smartEngine.getDeploymentQueryService();
        ActivityQueryService activityQueryService = smartEngine.getActivityQueryService();
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();
        ExecutionQueryService executionQueryService =  smartEngine.getExecutionQueryService();
        ExecutionCommandService executionCommandService =  smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTVIE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("type");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);


        // FIXME RepositoryQueryService ,ADN TEST;.
        //RepositoryCommandService repositoryCommandService = smartEngine
        //        .getRepositoryCommandService();
        //ProcessDefinition processDefinition = repositoryCommandService
        //        .deploy("multi-instance-test.bpmn20.xml");
        //assertEquals(9, processDefinition.getProcess().getElements().size());

        //FIXME 断言新的元素解析是否正确。新增的api，其他custom模式下需要增加判断。



        //4.启动流程实例

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpeicalKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");
        request.put(RequestMapSpeicalKeyConstant.PROCESS_DEFINITION_TYPE,"type");

        ProcessInstance processInstance = processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
              ,request  );
        Assert.assertNotNull(processInstance);
        Assert.assertEquals("type",processInstance.getProcessDefinitionType());


        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
        processInstanceQueryParam.setStartUserId("123");
        List<ProcessInstance> processInstanceList = processQueryService.findList(
            processInstanceQueryParam);
        Assert.assertNotNull(processInstanceList);
        Assert.assertTrue(processInstanceList.size()>=1 );

        processInstanceQueryParam.setProcessDefinitionType("type");
        processInstanceList = processQueryService.findList(
            processInstanceQueryParam);
        Assert.assertNotNull(processInstanceList);
        Assert.assertTrue(processInstanceList.size()>=1 );


        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setPageSize(2);
        taskInstanceQueryParam.setPageOffSide(0);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);
        taskInstanceQueryParam.setProcessInstanceId(processInstance.getInstanceId());

        taskInstanceQueryParam.setAssigneeUserId("1");
        List<String> assigneeGroupIdList = new ArrayList<String>();
        assigneeGroupIdList.add("a");
        assigneeGroupIdList.add("b");
        taskInstanceQueryParam.setAssigneeGroupIdList(assigneeGroupIdList);

        List<TaskInstance>  assertedTaskInstanceList=   taskQueryService.findList(taskInstanceQueryParam);
        Assert.assertEquals(1,assertedTaskInstanceList.size());


        assertedTaskInstanceList=   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),"1");
        Assert.assertEquals(1,assertedTaskInstanceList.size());




        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        //submitFormRequest.put("assigneeUserId","1");
        submitFormRequest.put(RequestMapSpeicalKeyConstant.TASK_INSTANCE_TAG,"tag");
        //submitFormRequest.put(RequestMapSpeicalKeyConstant.PROCESS_DEFINITION_TYPE,"type");



        //6.流程流转:处理 submitTask,完成任务申请.
        taskCommandService.complete(assertedTaskInstanceList.get(0).getInstanceId(),submitFormRequest);


        assertedTaskInstanceList=   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),"1");
        Assert.assertEquals(0,assertedTaskInstanceList.size());

        taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.COMPLETED);
        taskInstanceQueryParam.setAssigneeUserId("1");
        taskInstanceQueryParam.setTag("tag");
        taskInstanceQueryParam.setProcessDefinitionType("type");
        taskInstanceQueryParam.setProcessInstanceId(processInstance.getInstanceId());

        assertedTaskInstanceList=   taskQueryService.findList(taskInstanceQueryParam);
        Assert.assertEquals(1,assertedTaskInstanceList.size());
        Assert.assertEquals("tag",assertedTaskInstanceList.get(0).getTag());


        // 驱动 ReceiverTask
        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        Assert.assertEquals(1,activeExecutions.size());
        executionCommandService.signal(activeExecutions.get(0).getInstanceId());


        //7. 获取当前待处理任务.
        List<TaskInstance>   auditTaskInstanceList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance auditTaskInstance = auditTaskInstanceList.get(0);
        Map<String, Object> approveFormRequest = new HashMap<String, Object>();

        //10.
        approveFormRequest.put("approve", "agree");
        approveFormRequest.put("desc", "ok");

        //9.审批通过,驱动流程节点到自动执行任务环节

        taskCommandService.complete(auditTaskInstance.getInstanceId(),approveFormRequest);

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(auditTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }





}
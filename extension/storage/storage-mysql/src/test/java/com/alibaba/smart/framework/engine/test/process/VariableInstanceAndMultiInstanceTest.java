package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.bean.TestUser;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.util.IOUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VariableInstanceAndMultiInstanceTest extends DatabaseBaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(VariableInstanceAndMultiInstanceTest.class);


    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

        trace.clear();
    }


    public static final String AGREE = "agree";

    public static final String DISAGREE = "disagree";

    public static List<Object> trace=new ArrayList<Object>();



    @Test
    public void testMultiInstance() throws Exception {



        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("group");
        createDeploymentCommand.setProcessDefinitionCode("code");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertEquals("code",deploymentInstance.getProcessDefinitionCode());

        //4.启动流程实例

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"group");
        request.put("processVariable","processVariableValue");


        ProcessInstance processInstance = processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
              ,request  );
        Assert.assertNotNull(processInstance);
        Assert.assertEquals("group",processInstance.getProcessDefinitionType());


        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
        processInstanceQueryParam.setStartUserId("123");

        List<String> processInstanceIdList = new ArrayList<String>();
        processInstanceIdList.add(processInstance.getInstanceId());
        processInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);


        List<ProcessInstance> processInstanceList = processQueryService.findList(
            processInstanceQueryParam);
        Assert.assertNotNull(processInstanceList);
        Assert.assertTrue(processInstanceList.size() == 1 );

        processInstanceQueryParam.setProcessDefinitionType("group");
        processInstanceList = processQueryService.findList(
            processInstanceQueryParam);
        Assert.assertNotNull(processInstanceList);
        Assert.assertTrue(processInstanceList.size() == 1  );


        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setPageSize(2);
        taskInstanceQueryParam.setPageOffset(0);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);

        List<String> processInstanceIds = new ArrayList<String>();
        processInstanceIds.add(processInstance.getInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIds);

        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("1");
        List<String> assigneeGroupIdList = new ArrayList<String>();
        assigneeGroupIdList.add("a");
        assigneeGroupIdList.add("b");
        pendingTaskQueryParam.setAssigneeGroupIdList(assigneeGroupIdList);
        pendingTaskQueryParam.setProcessInstanceIdList(processInstanceIdList);



        List<TaskInstance>  assertedTaskInstanceList=   taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,assertedTaskInstanceList.size());


        assertedTaskInstanceList=   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,assertedTaskInstanceList.size());




        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        //submitFormRequest.put("assigneeUserId","1");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG,AGREE);
        submitFormRequest.put("text","123");

        //submitFormRequest.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"group");



        //6.触发会签审批通过，此时应该流转到 normalReceiverTask
        taskCommandService.complete(assertedTaskInstanceList.get(0).getInstanceId(),submitFormRequest);


        assertedTaskInstanceList=   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(0,assertedTaskInstanceList.size());

        taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.COMPLETED);
        //taskInstanceQueryParam.setClaimUserId("1");
        taskInstanceQueryParam.setTag(AGREE);
        taskInstanceQueryParam.setProcessDefinitionType("group");


        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIds);

        assertedTaskInstanceList=   taskQueryService.findList(taskInstanceQueryParam);
        Assert.assertEquals(1,assertedTaskInstanceList.size());
        Assert.assertEquals(AGREE,assertedTaskInstanceList.get(0).getTag());


        // 驱动 ReceiverTask
        List<ExecutionInstance> activeExecutions = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        Assert.assertEquals(1,activeExecutions.size());
        executionCommandService.signal(activeExecutions.get(0).getInstanceId());


        //7. 获取当前待处理任务.
        List<TaskInstance>   auditTaskInstanceList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        TaskInstance auditTaskInstance = auditTaskInstanceList.get(0);
        Map<String, Object> approveFormRequest = new HashMap<String, Object>();

        //10.
        approveFormRequest.put("action", AGREE);
        approveFormRequest.put("desc", "ok");
        approveFormRequest.put("text","789");

        approveFormRequest.put("boolean",true);


        approveFormRequest.put("double",11.22d);
        approveFormRequest.put("float",22.33f);

        approveFormRequest.put("long",5566L);
        approveFormRequest.put("short",123);
        approveFormRequest.put("integer",102400);
        approveFormRequest.put("user",new TestUser("userName","passWord"));

        List<VariableInstance> processInstanceVariableList = variableQueryService.findProcessInstanceVariableList(processInstance.getInstanceId());
        Assert.assertNotNull(processInstanceVariableList);
        Assert.assertEquals(1,processInstanceVariableList.size());




        //9.审批通过,驱动流程节点到自动执行任务环节

        taskCommandService.complete(auditTaskInstance.getInstanceId(),approveFormRequest);

        List<VariableInstance> processInstanceVariableList1 = variableQueryService.findList(processInstance.getInstanceId(),auditTaskInstance.getExecutionInstanceId());
        Assert.assertNotNull(processInstanceVariableList1);


        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(auditTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());

        LOGGER.info("magic"+trace.toString());

        Assert.assertEquals(2,trace.size());



        Assert.assertEquals("123",trace.get(0));
        Assert.assertEquals("789",trace.get(1));

    }





}
package com.alibaba.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.command.CreateDeploymentCommand;
import com.alibaba.smart.framework.engine.service.param.query.PaginateQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.util.IOUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MultiInstanceCompatibleAllModelPassedTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Test
    public void passed() throws Exception {


        String deploymentUserId = "123";
        String name = "name";
        String type = "group";
        String code = "code";
        String processDefinitionId = "test-multi-instance-user-task";
        String version = "1.0.1";
        String desc = "desc";

        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("compatible-all-passed.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId(deploymentUserId);
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc(desc);
        createDeploymentCommand.setProcessDefinitionName(name);
        createDeploymentCommand.setProcessDefinitionType(type);
        createDeploymentCommand.setProcessDefinitionCode(code);

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("compatible-all-passed.bpmn20.xml").getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.startWith(deploymentInstance.getInstanceId()
                );
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        submitFormRequest.put("action", "agree");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, VariableInstanceAndMultiInstanceTest.AGREE);



        //6.流程流转:处理 submitTask,完成任务申请.
        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("1");
        pendingTaskQueryParam.setProcessDefinitionType("group");
        PaginateQueryParam paginateQueryParam = new  PaginateQueryParam ();
        paginateQueryParam.setPageSize(10);
        paginateQueryParam.setPageOffset(0);

        List<TaskInstance>  taskInstances = taskQueryService.findPendingTaskList( pendingTaskQueryParam);

        Assert.assertEquals(1,taskInstances.size());

        taskCommandService.complete(taskInstances.get(0).getInstanceId(),submitFormRequest);

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(2,submitTaskInstanceList.size());

        pendingTaskQueryParam.setAssigneeUserId("3");
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"3");

        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);


        pendingTaskQueryParam.setAssigneeUserId("5");
        submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"5");
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);



        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(0,submitTaskInstanceList.size());


        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        taskInstanceQueryParam.setClaimUserId("1");
        taskInstanceQueryParam.setProcessDefinitionType("group");
        taskInstanceQueryParam.setTag(VariableInstanceAndMultiInstanceTest.AGREE);

        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstance.getInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setStatus(TaskInstanceConstant.COMPLETED);

        taskInstanceQueryParam.setPageSize(10);
        taskInstanceQueryParam.setPageOffset(0);
        List<TaskInstance> list = taskQueryService.findList(taskInstanceQueryParam);

        Assert.assertEquals(1,list.size());

    }


}
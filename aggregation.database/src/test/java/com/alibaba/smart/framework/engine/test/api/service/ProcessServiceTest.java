package com.alibaba.smart.framework.engine.test.api.service;

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
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;
import com.alibaba.smart.framework.engine.test.process.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProcessServiceTest {

    @Test
    public void test() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
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
        VariableQueryService variableQueryService = smartEngine.getVariableQueryService();
        ExecutionCommandService executionCommandService =  smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("type");
        createDeploymentCommand.setProcessDefinitionCode("code");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertEquals("code",deploymentInstance.getProcessDefinitionCode());

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

        processInstance =   processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals("type",processInstance.getProcessDefinitionType());
        Assert.assertEquals("test-multi-instance-user-task:1.0.1",processInstance.getProcessDefinitionIdAndVersion());
        Assert.assertEquals("test-multi-instance-user-task",processInstance.getProcessDefinitionId());
        Assert.assertEquals("1.0.1",processInstance.getProcessDefinitionVersion());

        Assert.assertEquals("123",processInstance.getStartUserId());
        Assert.assertEquals(InstanceStatus.running,processInstance.getStatus());
        Assert.assertNotNull(processInstance.getCompleteTime());
        Assert.assertNotNull(processInstance.getStartTime());
        Assert.assertNull(processInstance.getBizUniqueId());

        processCommandService.abort(processInstance.getInstanceId(),"reason");
        processInstance =   processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.aborted,processInstance.getStatus());
        Assert.assertEquals("reason",processInstance.getReason());

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        Assert.assertEquals(0,executionInstanceList.size());
        List<TaskInstance> taskInstanceList =   taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(0,taskInstanceList.size());



    }


    @Test
    public void testQuery() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
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
        VariableQueryService variableQueryService = smartEngine.getVariableQueryService();
        ExecutionCommandService executionCommandService =  smartEngine.getExecutionCommandService();


        //3. 部署流程定义
        CreateDeploymentCommand createDeploymentCommand = new CreateDeploymentCommand();
        String content = IOUtil.readResourceFileAsUTF8String("compatible-any-passed.bpmn20.xml");
        createDeploymentCommand.setProcessDefinitionContent(content);
        createDeploymentCommand.setDeploymentUserId("123");
        createDeploymentCommand.setDeploymentStatus(DeploymentStatusConstant.ACTIVE);
        createDeploymentCommand.setProcessDefinitionDesc("desc");
        createDeploymentCommand.setProcessDefinitionName("name");
        createDeploymentCommand.setProcessDefinitionType("type");
        createDeploymentCommand.setProcessDefinitionCode("code");

        DeploymentInstance deploymentInstance =  deploymentCommandService.createDeployment(createDeploymentCommand);

        Assert.assertEquals("code",deploymentInstance.getProcessDefinitionCode());

        //4.启动流程实例

        Map<String, Object> request = new HashMap();
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,"123");


        processCommandService.start(deploymentInstance.getInstanceId(),request  );

        processCommandService.start(deploymentInstance.getInstanceId(),request  );

        processCommandService.start(deploymentInstance.getInstanceId(),request  );


        processCommandService.start(deploymentInstance.getInstanceId(),request  );


        request.put(RequestMapSpecialKeyConstant.PROCESS_BIZ_UNIQUE_ID,"uniqueId");
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,"type");

        processCommandService.start(
            deploymentInstance.getProcessDefinitionId(), deploymentInstance.getProcessDefinitionVersion()
            ,request  );

        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam ();
        processInstanceQueryParam.setProcessDefinitionType("type");
        processInstanceQueryParam.setStatus(InstanceStatus.running.name());
        processInstanceQueryParam.setStartUserId("123");
        //processInstanceQueryParam.setBizUniqueId("uniqueId");
        processInstanceQueryParam.setPageOffset(4);
        processInstanceQueryParam.setPageSize(10);

        List<ProcessInstance> processInstanceList = processQueryService.findList(processInstanceQueryParam);
        Assert.assertEquals(1,processInstanceList.size());


        processInstanceQueryParam.setBizUniqueId("uniqueId");
        processInstanceQueryParam.setPageOffset(0);
        processInstanceQueryParam.setPageSize(10);
        processInstanceList = processQueryService.findList(processInstanceQueryParam);
        Assert.assertEquals(1,processInstanceList.size());

        processInstanceQueryParam.setBizUniqueId("uniqueId");
        processInstanceQueryParam.setPageOffset(1);
        processInstanceQueryParam.setPageSize(10);
        processInstanceList = processQueryService.findList(processInstanceQueryParam);
        Assert.assertEquals(0,processInstanceList.size());

    }

}
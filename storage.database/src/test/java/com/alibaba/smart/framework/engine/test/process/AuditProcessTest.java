package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskAssigneeDAO;
import com.alibaba.smart.framework.engine.persister.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskInstanceCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
public class AuditProcessTest {


    @Test
    public void testAuditProcess() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeService(new DefaultTaskAssigneeService());
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        TaskInstanceCommandService taskInstanceCommandService = smartEngine.getTaskCommandService();

        ProcessInstanceQueryService processQueryService = smartEngine.getProcessQueryService();
        ActivityInstanceQueryService activityQueryService = smartEngine.getActivityQueryService();
        TaskInstanceQueryService taskQueryService = smartEngine.getTaskQueryService();

        TaskAssigneeDAO taskAssigneeDAO= (TaskAssigneeDAO) SpringContextUtil.getBean("taskAssigneeDAO");


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");
        assertEquals(17, processDefinition.getProcess().getElements().size());

        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
                );
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTask(processInstance.getInstanceId());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put("assigneeId","1");

        //6.流程流转:处理 submitTask,完成任务申请.
        taskInstanceCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);

        //7. 获取当前待处理任务.
        List<TaskInstance>   auditTaskInstanceList = taskQueryService.findPendingTask(processInstance.getInstanceId());
        TaskInstance auditTaskInstance = auditTaskInstanceList.get(0);
        Map<String, Object> approveFormRequest = new HashMap<String, Object>();

        //10.
        approveFormRequest.put("approve", "agree");
        approveFormRequest.put("desc", "ok");

        //9.审批通过,驱动流程节点到自动执行任务环节

        taskInstanceCommandService.complete(auditTaskInstance.getInstanceId(),approveFormRequest);

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findOne(auditTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }


    @Test
    public void testFailedServiceTaskAuditProcess() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        TaskInstanceCommandService taskInstanceCommandService = smartEngine.getTaskCommandService();

        ProcessInstanceQueryService processQueryService = smartEngine.getProcessQueryService();
        ActivityInstanceQueryService activityQueryService = smartEngine.getActivityQueryService();
        TaskInstanceQueryService taskQueryService = smartEngine.getTaskQueryService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("failed-test-usertask-and-servicetask-exclusive.bpmn20.xml");
        assertEquals(17, processDefinition.getProcess().getElements().size());

        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
        );
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTask(processInstance.getInstanceId());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put("assigner","leader");

        //6.流程流转:处理 submitTask,完成任务申请.
        taskInstanceCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);

        //7. 获取当前待处理任务.
        List<TaskInstance>   auditTaskInstanceList = taskQueryService.findPendingTask(processInstance.getInstanceId());
        TaskInstance auditTaskInstance = auditTaskInstanceList.get(0);
        Map<String, Object> approveFormRequest = new HashMap<String, Object>();

        //10.
        approveFormRequest.put("approve", "agree");
        approveFormRequest.put("desc", "ok");

        //9.审批通过,驱动流程节点到自动执行任务环节

        taskInstanceCommandService.complete(auditTaskInstance.getInstanceId(),approveFormRequest);

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findOne(auditTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }


}
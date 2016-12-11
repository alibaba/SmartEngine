package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskInstanceQueryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
public class MixedAuditProcessTest {


    @Test
    public void testUserTaskExclusive() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setPersisteModel("mysql-tddl");
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessService();
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();

        ProcessInstanceQueryService processQueryService = smartEngine.getProcessQueryService();
        ActivityInstanceQueryService activityQueryService = smartEngine.getActivityQueryService();
        ExecutionInstanceQueryService executionQueryService = smartEngine.getExecutionQueryService();
        TaskInstanceQueryService taskQueryService = smartEngine.getTaskQueryService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");
        assertEquals(17, processDefinition.getProcess().getElements().size());



        //4.启动流程定义
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
                );
        Assert.assertNotNull(processInstance);

        //5.测试:断言流程节点正确
        List<ActivityInstance>  activityInstanceList =  activityQueryService.findAll(processInstance.getInstanceId());
        Assert.assertNotNull(activityInstanceList);
        Assert.assertEquals(2,activityInstanceList.size() );


        //6.测试:断言任务数正确
        List<TaskInstance> taskInstanceList=  taskQueryService.findPendingTask(processInstance.getInstanceId());
        Assert.assertNotNull(taskInstanceList);
        TaskInstance submitTaskInstance = taskInstanceList.get(0);
        Assert.assertNotNull(submitTaskInstance);


        //7.流程流转:提交申请
        Map<String, Object> submitFormRequest = new HashMap<>();
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put("assigner","leader"); //TODO 提供统一抽象和隔离,和公司账户体系集成

        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);

        //8.测试:新流程节点数据正确
        activityInstanceList =  activityQueryService.findAll(processInstance.getInstanceId());
        Assert.assertNotNull(activityInstanceList);
        Assert.assertEquals(3,activityInstanceList.size() );

        taskInstanceList = taskQueryService.findPendingTask(processInstance.getInstanceId());
        Assert.assertNotNull(taskInstanceList);

        TaskInstance approveTaskInstance = taskInstanceList.get(0);
        Assert.assertNotNull(approveTaskInstance);

        //9.审批通过,驱动流程节点到自动执行任务环节
        Map<String, Object> approveFormRequest = new HashMap<>();
        approveFormRequest.put("approve", "agree");
        approveFormRequest.put("desc", "ok");
        taskCommandService.complete(approveTaskInstance.getInstanceId(),approveFormRequest);

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findOne(approveTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());
        Assert.assertNotNull(finalProcessInstance.getCompleteDate());


        //11. TODO 处理加签流程,任务和人关联列表,流程变量存储,索引
    }


}
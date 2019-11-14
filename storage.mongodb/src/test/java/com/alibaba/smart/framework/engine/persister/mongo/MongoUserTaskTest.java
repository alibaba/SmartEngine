package com.alibaba.smart.framework.engine.persister.mongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.TableSchemaStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 高海军 帝奇 74394 on 2018 November  23:45.
 */

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MongoUserTaskTest {

    @Test
    public void test() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        TableSchemaStrategy tableSchemaStrategy = new CustomTableSchemaStrategy();
        processEngineConfiguration.setTableSchemaStrategy(tableSchemaStrategy);


        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
        ProcessQueryService processQueryService = smartEngine.getProcessQueryService();
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("mongo_simple_userTask_test.xml").getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start("simple-usertask-process","2.0.0");
        Assert.assertNotNull(processInstance);


        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("1");
        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());
        TaskInstance firstTaskInstance = submitTaskInstanceList.get(0);

        Assert.assertEquals("processPayment",firstTaskInstance.getProcessDefinitionActivityId());

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        taskCommandService.complete(firstTaskInstance.getInstanceId(),submitFormRequest);

        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());



    }
}


package com.alibaba.smart.framework.engine.test.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.ProcessInstanceModeConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskItemCommandService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.process.CaigouTaskItemCompleteProcessor;
import com.alibaba.smart.framework.engine.test.process.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.FullMultiInstanceTest;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.IdAndGroupTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;

public class TaskItemServiceTest {

    @Test
    public void test() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setTaskItemCompleteProcessor(new CaigouTaskItemCompleteProcessor());
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine.getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService.deploy("usertask-item-test.bpmn20.xml");

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.PROCESS_BIZ_UNIQUE_ID,"100");
        request.put(RequestMapSpecialKeyConstant.PROCESS_SUB_BIZ_UNIQUE_ID, Arrays.asList("101","102"));
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE, ProcessInstanceModeConstant.ITEM);
        ProcessInstance processInstance = processCommandService.start(processDefinition.getId(), processDefinition.getVersion(),request);
        Assert.assertNotNull(processInstance);

        //完成任务
        TaskItemCommandService taskItemCommandService = smartEngine.getTaskItemCommandService();
        request = new HashMap<String, Object>();
        request.put(RequestMapSpecialKeyConstant.TASK_ITEM_INSTANCE_TAG, FullMultiInstanceTest.AGREE);
        taskItemCommandService.complete("210003","101",request);
    }
}

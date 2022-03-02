package com.alibaba.smart.framework.engine.test.process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MultiInstanceCompatibleAnyModelPassedTest extends DatabaseBaseTestCase {

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


        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("compatible-any-passed.bpmn20.xml").getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
                );
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(3,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        submitFormRequest.put("action", "agree");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, VariableInstanceAndMultiInstanceTest.AGREE);

        //6.流程流转:处理 submitTask,完成任务申请.
        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);



        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(submitTaskInstance.getProcessInstanceId());
        Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());


    }

    @Test
    public void any(){
        ProcessDefinitionSource deploy = repositoryCommandService
                .deploy("huiqian.xml");
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        Map<String, Object> initRequest = new HashMap<String, Object>();
        initRequest.put("userIds", Arrays.asList(1195351L, 1L));
        ProcessInstance processInstance= processCommandService.start("huiqian_1","1.0.0", initRequest);
        TaskQueryService taskQueryService = smartEngine.getTaskQueryService();
        List<TaskInstance> taskList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Map<String, Object> submitRequest = new HashMap<String, Object>();
        submitRequest.put("title", "111");
        submitRequest.put("desc", "ok");
        submitRequest.put("completeCond", 2);
        smartEngine.getTaskCommandService().complete(taskList.get(0).getInstanceId(), submitRequest);
        taskList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        System.out.println(taskList.size());
    }


}
package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MultiInstanceCompatibleAnyModelFailedFastWithTenantIdTest extends DatabaseBaseTestCase {

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

        String tenantId = "-1";
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("compatible-any-passed.bpmn20.xml",tenantId).getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),tenantId
                );
        Assert.assertNotNull(processInstance);

        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());

        Assert.assertEquals(3,submitTaskInstanceList.size());
        TaskInstance submitTaskInstance = submitTaskInstanceList.get(0);

        //5.流程流转:构造提交申请参数
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("title", "new_title");
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,"1");
        submitFormRequest.put("action", "agree");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, VariableInstanceAndMultiInstanceTest.DISAGREE);

        //6.流程流转:处理 submitTask,完成任务申请.
        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);


        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertEquals(2,submitTaskInstanceList.size());
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertEquals(1,submitTaskInstanceList.size());
        taskCommandService.complete(submitTaskInstanceList.get(0).getInstanceId(),submitFormRequest);

        submitTaskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertEquals(0,submitTaskInstanceList.size());


        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(submitTaskInstance.getProcessInstanceId(),processInstance.getTenantId());
        Assert.assertEquals(InstanceStatus.aborted,finalProcessInstance.getStatus());


    }


}
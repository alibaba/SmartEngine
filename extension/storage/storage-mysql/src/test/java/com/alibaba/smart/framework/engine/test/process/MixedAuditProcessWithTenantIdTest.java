package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
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

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MixedAuditProcessWithTenantIdTest extends DatabaseBaseTestCase {


    @Test
    public void testUserTaskExclusive() throws Exception {
        String tenantId = "-1";

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml",tenantId).getFirstProcessDefinition();
        assertEquals(17, processDefinition.getBaseElementList().size());



        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),tenantId
                );
        Assert.assertNotNull(processInstance);

        //5.测试:断言流程节点正确
        List<ActivityInstance>  activityInstanceList =  activityQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(activityInstanceList);
        assertEquals(2,activityInstanceList.size() );


        //6.测试:断言任务数正确
        List<TaskInstance> taskInstanceList=  taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(taskInstanceList);
        TaskInstance submitTaskInstance = taskInstanceList.get(0);
        Assert.assertNotNull(submitTaskInstance);


        //7.流程流转:提交申请
        Map<String, Object> submitFormRequest = new HashMap<String, Object>();
        submitFormRequest.put("qps", "300");
        submitFormRequest.put("capacity","10g");
        submitFormRequest.put("assigner","leader");
        submitFormRequest.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);

        taskCommandService.complete(submitTaskInstance.getInstanceId(),submitFormRequest);

        //8.测试:新流程节点数据正确
        activityInstanceList =  activityQueryService.findAll(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(activityInstanceList);
        assertEquals(3,activityInstanceList.size() );

        taskInstanceList = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId(),processInstance.getTenantId());
        Assert.assertNotNull(taskInstanceList);

        TaskInstance approveTaskInstance = taskInstanceList.get(0);
        Assert.assertNotNull(approveTaskInstance);

        //9.审批通过,驱动流程节点到自动执行任务环节
        Map<String, Object> approveFormRequest = new HashMap<String, Object>();
        approveFormRequest.put("approve", "agree");
        approveFormRequest.put("desc", "ok");
        approveFormRequest.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);
        taskCommandService.complete(approveTaskInstance.getInstanceId(),approveFormRequest);

        //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
        ProcessInstance finalProcessInstance = processQueryService.findById(approveTaskInstance.getProcessInstanceId(),approveTaskInstance.getTenantId());
        assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());
        Assert.assertNotNull(finalProcessInstance.getCompleteTime());


    }


}
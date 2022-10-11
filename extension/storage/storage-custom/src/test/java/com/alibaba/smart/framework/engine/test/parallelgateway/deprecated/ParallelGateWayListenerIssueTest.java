package com.alibaba.smart.framework.engine.test.parallelgateway.deprecated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.persister.util.InstanceSerializerFacade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
  * @author chenhai
  * @date 2021/03/02
 */
public class ParallelGateWayListenerIssueTest extends ParallelGateWayListenerIssueConfiguration {


    @Before
    public void deploy() {
        repositoryCommandService.deploy("subprocess/parent.xml");
        repositoryCommandService.deploy("subprocess/child.xml");
    }

    @Override
    public ProcessEngineConfiguration initProcessEngineConfiguration() {

        ProcessEngineConfiguration processEngineConfiguration = super.initProcessEngineConfiguration();

        processEngineConfiguration.setExecutorService(
            Executors.newFixedThreadPool(20)
        );

        return processEngineConfiguration;

    }



    // 流程实例
    ProcessInstance parentProcessInstance = null;
    ProcessInstance childProcessInstance = null;

    // 流程实例id
    String parentProcessInstanceId = null;
    String childProcessInstanceId = null;

    // 流程执行实例列表
    List<ExecutionInstance> parentExecutionInstances = null;
    List<ExecutionInstance> childExecutionInstances = null;

    @Test
    public void test() {

        //-------------start--------------------

        PersisterSession.create();

        Assert.assertNotNull(PersisterSession.currentSession());

        Map<String, Object> request = new HashMap();
        request.put("person", "chenhai");

        // 创建流程
        parentProcessInstanceId = smartEngine.getProcessCommandService().start(
            "parent", "1", request
        ).getInstanceId();

        // 模拟启停过程
        destroySessionAndCreateNew();

        Assert.assertNotNull(parentProcessInstanceId);

        // 主流程存在
        Assert.assertEquals(1, PersisterSession.currentSession().getProcessInstances().size());

        parentExecutionInstances = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        // 执行到task1
        Assert.assertEquals("task_1", parentExecutionInstances.get(0).getProcessDefinitionActivityId());

        executionCommandService.signal(parentExecutionInstances.get(0).getInstanceId(), null);

        // 模拟启停过程
        destroySessionAndCreateNew();

        // 分支发散，一个到task_2，一个到子流程
        Assert.assertEquals(2, PersisterSession.currentSession().getProcessInstances().size());

        // 获取子流程id
        for (Map.Entry<String, ProcessInstance> entry : PersisterSession.currentSession().getProcessInstances()
            .entrySet()) {
            if (entry.getKey().equals(parentProcessInstanceId)) {
                continue;
            }
            childProcessInstanceId = entry.getKey();
            childProcessInstance = PersisterSession.currentSession().getProcessInstance(childProcessInstanceId);
        }

        Assert.assertNotNull(childProcessInstanceId);
        Assert.assertEquals(childProcessInstance.getParentInstanceId(), parentProcessInstanceId);

        // 找到子流程中的任务
        childExecutionInstances = executionQueryService.findActiveExecutionList(
            childProcessInstanceId);

        Assert.assertEquals(1, childExecutionInstances.size());

        ExecutionInstance task3 = childExecutionInstances.get(0);

        Assert.assertEquals("task_3", task3.getProcessDefinitionActivityId());

        // 驱动子流程中的任务
        executionCommandService.signal(task3.getInstanceId());

        // 模拟启停过程
        destroySessionAndCreateNew();

        // 到达task4和task5
        childExecutionInstances = executionQueryService.findActiveExecutionList(
            childProcessInstanceId);
        Assert.assertEquals(2, childExecutionInstances.size());

        ExecutionInstance task4 = null, task5 = null;
        // 找到task4和task5
        for (ExecutionInstance executionInstance : executionQueryService.findActiveExecutionList(
            childProcessInstanceId)) {

            if(executionInstance.getProcessDefinitionActivityId().equals("task_4")){
                task4 = executionInstance;
            }else if(executionInstance.getProcessDefinitionActivityId().equals("task_5")){
                task5 = executionInstance;
            }else {
                Assert.fail();
            }
        }

        Assert.assertNotNull(task4);
        Assert.assertNotNull(task5);
        // 激活 task4
        executionCommandService.signal(task4.getInstanceId());

        // 模拟启停过程
        destroySessionAndCreateNew();

        // 判断一下两个流程状态都为running
        parentProcessInstance = PersisterSession.currentSession().getProcessInstance(parentProcessInstanceId);
        childProcessInstance = PersisterSession.currentSession().getProcessInstance(childProcessInstanceId);

        Assert.assertEquals(InstanceStatus.running, parentProcessInstance.getStatus());
        Assert.assertEquals(InstanceStatus.running, childProcessInstance.getStatus());
        // 激活 task5
        executionCommandService.signal(task5.getInstanceId());

        // 模拟启停过程
        destroySessionAndCreateNew();

        // 获取最新两个流程实例
        parentProcessInstance = PersisterSession.currentSession().getProcessInstance(parentProcessInstanceId);
        childProcessInstance = PersisterSession.currentSession().getProcessInstance(childProcessInstanceId);

        // 判断子流程结束
        Assert.assertEquals(InstanceStatus.completed, childProcessInstance.getStatus());

        // 主流程还在继续
        Assert.assertEquals(InstanceStatus.running, parentProcessInstance.getStatus());

        // 回到主流程，剩下join网关和task2
        parentExecutionInstances = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(2, parentExecutionInstances.size());

        ExecutionInstance gatewayFork = null;
        ExecutionInstance task2 = null;


        for (ExecutionInstance executionInstance : parentExecutionInstances) {

            if(executionInstance.getProcessDefinitionActivityId().equals("Gateway_join")){
                gatewayFork = executionInstance;
            }else if(executionInstance.getProcessDefinitionActivityId().equals("task_2")){
                task2 = executionInstance;
            }else {
                Assert.fail();
            }


        }



        Assert.assertNotNull(gatewayFork);
        Assert.assertNotNull(task2);
        // 激活task2
        executionCommandService.signal(task2.getInstanceId());

        // 模拟启停过程
        destroySessionAndCreateNew();

        // 获取主流程实例
        parentProcessInstance = PersisterSession.currentSession().getProcessInstance(parentProcessInstanceId);

        // 判断主流程结束
        Assert.assertEquals(InstanceStatus.completed, parentProcessInstance.getStatus());

        PersisterSession.destroySession();

    }



    public void destroySessionAndCreateNew() {

        // 获得目前信息
        Collection<ProcessInstance> list = PersisterSession.currentSession().getProcessInstances().values();

        // 序列化存档
        List<String> data = new ArrayList();
        for (ProcessInstance processInstance : list) {
            data.add(InstanceSerializerFacade.serialize(processInstance));
        }

        // 重建session
        PersisterSession.destroySession();

        // 清空相关对象指针，防止误拿到之前的信息
        parentProcessInstance = null;
        childProcessInstance = null;
        parentExecutionInstances = null;
        childExecutionInstances = null;

        PersisterSession.create();

        // 恢复
        for (String each : data) {
            ProcessInstance processInstance = InstanceSerializerFacade.deserializeAll(each);

            PersisterSession.currentSession().putProcessInstance(processInstance);
        }

    }

}
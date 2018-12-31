package com.alibaba.smart.framework.engine.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:31.
 */
public class CallActivityProcessTest extends CustomBaseTest {

    @Test
    public void test() throws Exception {

        // 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("parent-callactivity-process.bpmn20.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());

        processDefinition = repositoryCommandService
            .deploy("callactivity-process.bpmn20.xml");
        assertEquals(7, processDefinition.getProcess().getElements().size());




        ProcessInstance processInstance =   startProcess();



        ExecutionInstance  firstExecutionInstance = findAndAssert(processInstance);




        PersisterSession.create().putProcessInstance(processInstance);


        //触发主流程执行进入到子流程里面
        executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);


        //START 111 因为执行到了callActivity节点,有主流程和子流程两个实例.获得子流程实例 id。

        Set<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(2, processInstanceIds.size());

        String parentProcessInstanceId =   processInstance.getInstanceId();
        String subProcessInstanceId = null;

        for (String instanceId : processInstanceIds) {
            if (!parentProcessInstanceId.equals(instanceId)) {
                subProcessInstanceId = instanceId;
            }
        }

        //PersisterSession.destroySession();


        //END 111



        //TODO 这里需要断言父子数据均是正确。

        // 主流程在callActivity节点
        List<ExecutionInstance > executionInstanceList =executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        assertEquals(1, executionInstanceList.size());
        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        // 子流程在debit节点
        List<ExecutionInstance > subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(1, subExecutionInstanceList.size());
        ExecutionInstance firstSubExecutionInstance = subExecutionInstanceList.get(0);
        assertTrue("debit".equals(firstSubExecutionInstance.getProcessDefinitionActivityId()));

        // TODO ettear 如果这时执行主流程应该异常
        //processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);


        //完成下单确认,将流程驱动到等待资金到账环节。
        executionCommandService.signal(firstSubExecutionInstance.getInstanceId());

        // 主流程还是在callActivity节点
        executionInstanceList =executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        assertEquals(1, executionInstanceList.size());
        firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //子流程在checkDebitResult节点
        subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(1, subExecutionInstanceList.size());
        firstSubExecutionInstance = subExecutionInstanceList.get(0);
        assertTrue("checkDebitResult".equals(firstSubExecutionInstance.getProcessDefinitionActivityId()));

        //完成资金到账,将流程驱动到资金交割处理环节。
        executionCommandService.signal(firstSubExecutionInstance.getInstanceId());

        //主流程结束callActivity节点，在end_order节点
        executionInstanceList = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("end_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //子流程结束
        ProcessInstance subProcessInstance= processQueryService.findById(subProcessInstanceId);
        Assert.assertEquals(InstanceStatus.completed,subProcessInstance.getStatus());
        subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(0, subExecutionInstanceList.size());

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        PersisterSession.destroySession();


    }

    public void signalForDivingIntoCallActivity(ExecutionInstance firstExecutionInstance, ProcessInstance processInstance) {


    }

    private ProcessInstance startProcess(){
        PersisterSession.create();

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction","pre_order");

        ProcessInstance processInstance = processCommandService.start(
            "parent-callactivity", "1.0.0",request
        );
        PersisterSession.destroySession();

        return processInstance;
    }

    private ExecutionInstance  findAndAssert ( ProcessInstance oldProcessInstance ){
        PersisterSession.create().putProcessInstance(oldProcessInstance);



        ProcessInstance latestProcessInstance =  processQueryService.findById(oldProcessInstance.getInstanceId());

        Assert.assertNotNull(InstanceStatus.running.equals(latestProcessInstance.getStatus()));

        //只有主流程一个实例
        Collection<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(1, processInstanceIds.size());


        List<ExecutionInstance> executionInstanceList =   executionQueryService.findActiveExecutionList(oldProcessInstance.getInstanceId());
        Assert.assertEquals(1,executionInstanceList.size());

        ExecutionInstance  firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("pre_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        PersisterSession.destroySession();

        return firstExecutionInstance;

    }



    @Test
    public void testAutomaticCallActivity() throws Exception {

        // 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("parent-callactivity-process.bpmn20.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());

        processDefinition = repositoryCommandService
            .deploy("callactivity-servicetask-process.bpmn20.xml");
        assertEquals(7, processDefinition.getProcess().getElements().size());




        ProcessInstance processInstance =   startProcess();



        ExecutionInstance  firstExecutionInstance = findAndAssert(processInstance);




        PersisterSession.create().putProcessInstance(processInstance);


        //触发主流程执行进入到子流程里面
        executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);


        //START 111 因为执行到了callActivity节点,有主流程和子流程两个实例.获得子流程实例 id。

        Set<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(2, processInstanceIds.size());

        String parentProcessInstanceId =   processInstance.getInstanceId();
        String subProcessInstanceId = null;

        for (String instanceId : processInstanceIds) {
            if (!parentProcessInstanceId.equals(instanceId)) {
                subProcessInstanceId = instanceId;
            }
        }

        //PersisterSession.destroySession();


        //END 111





        //主流程结束callActivity节点，在end_order节点
        List<ExecutionInstance >  executionInstanceList = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("end_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        //子流程结束
        ProcessInstance subProcessInstance= processQueryService.findById(subProcessInstanceId);
        Assert.assertEquals(InstanceStatus.completed,subProcessInstance.getStatus());
        List<ExecutionInstance >  subExecutionInstanceList = executionQueryService.findActiveExecutionList(subProcessInstanceId);
        assertEquals(0, subExecutionInstanceList.size());

        //完成资金交割处理,将流程驱动到ACK确认环节。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId());

        //测试下是否符合预期
        executionInstanceList =executionQueryService.findActiveExecutionList(processInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, processInstance.getStatus());

        PersisterSession.destroySession();


    }


}

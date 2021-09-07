package com.alibaba.smart.framework.engine.test.cases;

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
 *
 */
public class CustomCallActivityTest extends CustomBaseTestCase {

    @Test
    public void testBasicCallActivityWhenChildProcessIsPaused()  {

        //=======1.0 进入到子流程 =========
        ProduceProcessInstanceIds produceProcessInstanceIds = new ProduceProcessInstanceIds().invoke();

        String parentProcessInstanceId = produceProcessInstanceIds.getParentProcessInstanceId();
        String childProcessInstanceId = produceProcessInstanceIds.getChildProcessInstanceId();


        ExecutionInstance debitChildExecutionInstance = assertAndGetExecutionInstance(parentProcessInstanceId,
            childProcessInstanceId);

        //=======1.0 END =========

        //=======2.0 START ========= 这里需要驱动子流程运作。

        //完成下单确认,将流程驱动到等待资金到账环节。
        executionCommandService.signal(debitChildExecutionInstance.getInstanceId());

        // 父流程还是在callActivity节点（其实没啥必要再断言了）
        List<ExecutionInstance> parentExecutionInstanceList1 = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        assertEquals(1, parentExecutionInstanceList1.size());
        ExecutionInstance firstExecutionInstance2 = parentExecutionInstanceList1.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance2.getProcessDefinitionActivityId()));

        // 子流程在checkDebitResult节点
        List<ExecutionInstance> childExecutionInstanceList3 = executionQueryService.findActiveExecutionList(childProcessInstanceId);
        assertEquals(1, childExecutionInstanceList3.size());
        ExecutionInstance debitChildExecutionInstance3 = childExecutionInstanceList3.get(0);
        assertTrue("checkDebitResult".equals(debitChildExecutionInstance3.getProcessDefinitionActivityId()));


        //=======2.0 END ========= 这里需要驱动子流程运作。


        //=======3.0 START ========= 完成驱动子流程，此时流程需要回到父流程。

        //完成资金到账,将流程驱动到资金交割处理环节。

        executionCommandService.signal(debitChildExecutionInstance3.getInstanceId());

        //当子流程结束后，父主流程将从callActivity回到下一个节点。在本例中，callActivity的下一个节点是在end_order节点
        List<ExecutionInstance> endOrderExecutionInstanceList = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        assertEquals(1, endOrderExecutionInstanceList.size());
        ExecutionInstance endOrderExecutionInstance = endOrderExecutionInstanceList.get(0);
        assertTrue("end_order".equals(endOrderExecutionInstance.getProcessDefinitionActivityId()));

        //断言子流程结束
        ProcessInstance subProcessInstance = processQueryService.findById(childProcessInstanceId);
        Assert.assertEquals(InstanceStatus.completed, subProcessInstance.getStatus());
        List<ExecutionInstance> childExecutionInstanceList4 = executionQueryService.findActiveExecutionList(childProcessInstanceId);
        assertEquals(0, childExecutionInstanceList4.size());

        //完成资金交割处理,将流程驱动到ACK确认环节。
        ProcessInstance   processInstance4 = executionCommandService.signal(endOrderExecutionInstance.getInstanceId());

        //测试下是否符合预期
        List<ExecutionInstance> parentExecutionInstanceList4 = executionQueryService.findActiveExecutionList(processInstance4.getInstanceId());
        assertEquals(0, parentExecutionInstanceList4.size());

        assertEquals(InstanceStatus.completed, processInstance4.getStatus());

        PersisterSession.destroySession();

    }

    private ExecutionInstance assertAndGetExecutionInstance(String parentProcessInstanceId, String childProcessInstanceId) {
        // 此时，父流程在callActivity节点


        List<ExecutionInstance> parentExecutionInstanceList = executionQueryService.findActiveExecutionList(
            parentProcessInstanceId);
        assertEquals(1, parentExecutionInstanceList.size());
        ExecutionInstance firstExecutionInstance = parentExecutionInstanceList.get(0);
        assertTrue("callActivity".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        // 此时，子流程在debit节点
        List<ExecutionInstance> childExecutionInstanceList = executionQueryService.findActiveExecutionList(
            childProcessInstanceId);
        assertEquals(1, childExecutionInstanceList.size());
        ExecutionInstance debitChildExecutionInstance = childExecutionInstanceList.get(0);
        assertTrue("debit".equals(debitChildExecutionInstance.getProcessDefinitionActivityId()));
        return debitChildExecutionInstance;
    }

    //@Test
    //public void signalCallActivityForException() {
    //
    //
    //    //=======1.0 进入到子流程 =========
    //    ProduceProcessInstanceIds produceProcessInstanceIds = new ProduceProcessInstanceIds().invoke();
    //
    //    String parentProcessInstanceId = produceProcessInstanceIds.getParentProcessInstanceId();
    //
    //
    //    List<ExecutionInstance> parentExecutionInstanceList = executionQueryService.findActiveExecutionList(
    //        parentProcessInstanceId);
    //    assertEquals(1, parentExecutionInstanceList.size());
    //    ExecutionInstance callActivityExecutionInstance = parentExecutionInstanceList.get(0);
    //    assertTrue("callActivity".equals(callActivityExecutionInstance.getProcessDefinitionActivityId()));
    //
    //    //=======1.0 END =========
    //
    //    // 如果执行父流程，应该要抛异常。
    //    try {
    //        ProcessInstance processInstance=   executionCommandService.signal(callActivityExecutionInstance.getInstanceId());
    //
    //    }catch (EngineException e){
    //        Assert.assertTrue(e instanceof SignalException);
    //    }
    //
    //    fail();
    //}

    private ProcessInstance startParentProcess(ProcessDefinition parentProcessDefinition) {
        PersisterSession.create();

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction", "pre_order");

        ProcessInstance processInstance = processCommandService.start(
            parentProcessDefinition.getId(), parentProcessDefinition.getVersion(), request
        );
        PersisterSession.destroySession();

        return processInstance;
    }

    private ExecutionInstance findAndAssertAtPreOrderPhase(ProcessInstance oldProcessInstance) {
        PersisterSession.create().putProcessInstance(oldProcessInstance);

        ProcessInstance latestProcessInstance = processQueryService.findById(oldProcessInstance.getInstanceId());

        Assert.assertNotNull(InstanceStatus.running.equals(latestProcessInstance.getStatus()));

        //只有主流程一个实例
        Collection<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(1, processInstanceIds.size());

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            oldProcessInstance.getInstanceId());
        Assert.assertEquals(1, executionInstanceList.size());

        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("pre_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));

        PersisterSession.destroySession();

        return firstExecutionInstance;

    }

    @Test
    public void testAutomaticCallActivity()  {

        // 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition parentProcessDefinition = repositoryCommandService
            .deploy("parent-callactivity-process.bpmn20.xml").getFirstProcessDefinition();
        assertEquals(14, parentProcessDefinition.getBaseElementList().size());

        ProcessDefinition childProcessDefinition = repositoryCommandService
            .deploy("callactivity-servicetask-process.bpmn20.xml").getFirstProcessDefinition();
        assertEquals(7, childProcessDefinition.getBaseElementList().size());

        ProcessInstance parentProcessInstance = startParentProcess(parentProcessDefinition);

        ExecutionInstance parentExecutionInstance = findAndAssertAtPreOrderPhase(parentProcessInstance);

        PersisterSession.create().putProcessInstance(parentProcessInstance);

        //触发主流程执行进入到子流程里面
        executionCommandService.signal(parentExecutionInstance.getInstanceId(), null);

        //START 111 因为执行到了callActivity节点,有主流程和子流程两个实例.获得子流程实例 id。

        Set<String> processInstanceIds = PersisterSession.currentSession().getProcessInstances().keySet();
        Assert.assertEquals(2, processInstanceIds.size());

        String parentProcessInstanceId = parentProcessInstance.getInstanceId();
        String subProcessInstanceId = null;

        for (String instanceId : processInstanceIds) {
            if (!parentProcessInstanceId.equals(instanceId)) {
                subProcessInstanceId = instanceId;
            }
        }

        //PersisterSession.destroySession();

        //END 111

        //主流程结束callActivity节点，在end_order节点
        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            parentProcessInstanceId);
        ExecutionInstance parentExecutionInstance1 = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("end_order".equals(parentExecutionInstance1.getProcessDefinitionActivityId()));

        //子流程结束
        ProcessInstance subProcessInstance = processQueryService.findById(subProcessInstanceId);
        Assert.assertEquals(InstanceStatus.completed, subProcessInstance.getStatus());
        List<ExecutionInstance> subExecutionInstanceList = executionQueryService.findActiveExecutionList(
            subProcessInstanceId);
        assertEquals(0, subExecutionInstanceList.size());

        //完成资金交割处理,将流程驱动到ACK确认环节。
        parentProcessInstance = executionCommandService.signal(parentExecutionInstance1.getInstanceId());

        //测试下是否符合预期
        executionInstanceList = executionQueryService.findActiveExecutionList(parentProcessInstance.getInstanceId());
        assertEquals(0, executionInstanceList.size());

        assertEquals(InstanceStatus.completed, parentProcessInstance.getStatus());

    }

    private class ProduceProcessInstanceIds {
        private String parentProcessInstanceId;
        private String childProcessInstanceId;

        public String getParentProcessInstanceId() {
            return parentProcessInstanceId;
        }

        public String getChildProcessInstanceId() {
            return childProcessInstanceId;
        }

        public ProduceProcessInstanceIds invoke() {
            // 部署流程定义
            RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
            ProcessDefinition parentProcessDefinition = repositoryCommandService
                .deploy("parent-callactivity-process.bpmn20.xml").getFirstProcessDefinition();
            assertEquals(14, parentProcessDefinition.getBaseElementList().size());

            ProcessDefinition childProcessDefinition = repositoryCommandService
                .deploy("child-callactivity-process.bpmn20.xml").getFirstProcessDefinition();
            assertEquals(7, childProcessDefinition.getBaseElementList().size());

            ProcessInstance processInstance = startParentProcess(  parentProcessDefinition);

            //=======1 =========

            ExecutionInstance firstExecutionInstance = findAndAssertAtPreOrderPhase(processInstance);

            PersisterSession.create().putProcessInstance(processInstance);

            //=======1.1 ========= 触发主流程执行进入到子流程里面
            executionCommandService.signal(firstExecutionInstance.getInstanceId());

            //=======1.2 ========= START 111 因为执行到了callActivity节点,有主流程和子流程两个实例.获得子流程实例 id。

            Map<String, ProcessInstance> processInstances = PersisterSession.currentSession().getProcessInstances();
            Set<String> processInstanceIds = processInstances.keySet();
            Assert.assertEquals(2, processInstanceIds.size());

            parentProcessInstanceId = processInstance.getInstanceId();
            childProcessInstanceId = null;

            for (String instanceId : processInstanceIds) {
                if (!parentProcessInstanceId.equals(instanceId)) {
                    childProcessInstanceId = instanceId;
                }
            }
            return this;
        }
    }
}

package com.alibaba.smart.framework.engine.test.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by 高海军 帝奇 74394 on 2017 May  16:31.
 *
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DataBaseCallActivityTest extends DatabaseBaseTestCase {

    @Test
    public void testBasicCallActivityWhenChildProcessIsPaused()  {

        //=======1.0 进入到子流程 =========
        ProduceProcessInstanceIds produceProcessInstanceIds = new ProduceProcessInstanceIds().invoke();

        String parentProcessInstanceId = produceProcessInstanceIds.getParentProcessInstanceId();
        String childProcessInstanceId = produceProcessInstanceIds.getChildProcessInstanceId();


        ExecutionInstance childDebitExecutionInstance = assertAndGetExecutionInstance(parentProcessInstanceId,
            childProcessInstanceId);

        //=======1.0 END =========

        //=======2.0 START ========= 这里需要驱动子流程运作。

        //完成下单确认,将流程驱动到等待资金到账环节。
        executionCommandService.signal(childDebitExecutionInstance.getInstanceId());

        // 父流程还是在callActivity节点（其实没啥必要再断言了）
        List<ExecutionInstance> parentCallActivityExecutionInstanceList = executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        assertEquals(1, parentCallActivityExecutionInstanceList.size());
        ExecutionInstance parentCallActivityExecutionInstance = parentCallActivityExecutionInstanceList.get(0);
        assertTrue("callActivity".equals(parentCallActivityExecutionInstance.getProcessDefinitionActivityId()));

        // 子流程在checkDebitResult节点
        List<ExecutionInstance> childExecutionInstanceList = executionQueryService.findActiveExecutionList(childProcessInstanceId);
        assertEquals(1, childExecutionInstanceList.size());
        ExecutionInstance childDebitChildExecutionInstance = childExecutionInstanceList.get(0);
        assertTrue("checkDebitResult".equals(childDebitChildExecutionInstance.getProcessDefinitionActivityId()));


        //=======2.0 END ========= 这里需要驱动子流程运作。


        //=======3.0 START ========= 完成驱动子流程，此时流程需要回到父流程。

        //完成资金到账,将流程驱动到资金交割处理环节。
        executionCommandService.signal(childDebitChildExecutionInstance.getInstanceId());

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



    private ProcessInstance startParentProcess(ProcessDefinition parentProcessDefinition) {

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("smartEngineAction", "pre_order");

        ProcessInstance processInstance = processCommandService.start(
            parentProcessDefinition.getId(), parentProcessDefinition.getVersion(), request
        );

        return processInstance;
    }

    private ExecutionInstance findAndAssertAtPreOrderPhase(ProcessInstance oldProcessInstance) {

        ProcessInstance parentProcessInstance = processQueryService.findById(oldProcessInstance.getInstanceId());

        Assert.assertNotNull(InstanceStatus.running.equals(parentProcessInstance.getStatus()));

        //只有主流程一个实例
        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
        processInstanceQueryParam.setStatus(InstanceStatus.running.name());

        List<ProcessInstance> processInstanceList = processQueryService.findList(processInstanceQueryParam);
        Assert.assertEquals(1, processInstanceList.size());

        List<ExecutionInstance> executionInstanceList = executionQueryService.findActiveExecutionList(
            oldProcessInstance.getInstanceId());
        Assert.assertEquals(1, executionInstanceList.size());

        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        assertTrue("pre_order".equals(firstExecutionInstance.getProcessDefinitionActivityId()));


        return firstExecutionInstance;

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

            ProcessInstance parentProcessInstance = startParentProcess(  parentProcessDefinition);

            //=======1 =========

            ExecutionInstance firstExecutionInstance = findAndAssertAtPreOrderPhase(parentProcessInstance);


            //=======1.1 ========= 触发主流程执行进入到子流程里面
            executionCommandService.signal(firstExecutionInstance.getInstanceId());

            //=======1.2 ========= START 111 因为执行到了callActivity节点,有主流程和子流程两个实例.获得子流程实例 id。

            ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
            processInstanceQueryParam.setStatus(InstanceStatus.running.name());

            List<ProcessInstance> processInstanceList = processQueryService.findList(processInstanceQueryParam);
            Assert.assertEquals(2, processInstanceList.size());


            parentProcessInstanceId = parentProcessInstance.getInstanceId();
            childProcessInstanceId = null;

            for (ProcessInstance instance : processInstanceList) {
                if (!parentProcessInstanceId.equals(instance.getInstanceId())) {
                    childProcessInstanceId = instance.getInstanceId();
                }
            }


            //ADD ASSERT
            ProcessInstanceQueryParam parentProcessInstanceQueryParam = new ProcessInstanceQueryParam();
            parentProcessInstanceQueryParam.setParentInstanceId(parentProcessInstance.getInstanceId());
            List<ProcessInstance> result = processQueryService.findList(parentProcessInstanceQueryParam);
            Assert.assertEquals(1, result.size());
            ProcessInstance childProcessInstance = result.get(0);

            Assert.assertEquals(childProcessInstanceId,childProcessInstance.getInstanceId());
            Assert.assertNotNull(childProcessInstance.getParentExecutionInstanceId());

            return this;
        }
    }
}

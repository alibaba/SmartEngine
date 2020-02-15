package com.alibaba.smart.framework.engine.test.jump;

import java.util.HashMap;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author shiyang.xsy
 * @date 2017/12/6
 */
public class JumpFreeNode1Test  extends CustomBaseTestCase {

    @Test
    public void test() {
        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

        repositoryCommandService
            .deploy("smart-engine/jump1.bpmn20.xml");
        //start
        ProcessInstance processInstance = smartEngine.getProcessCommandService().start(
            "jump1-process", "1.0.0", new HashMap<String, Object>()
        );

        Assert.assertNotNull(processInstance);

        String processInstanceId = processInstance.getInstanceId();
        List<ExecutionInstance> executionInstances;

        //assert task1
        executionInstances = this.executionQueryService.findActiveExecutionList(processInstanceId);
        Assert.assertEquals(1, executionInstances.size());

        Assert.assertEquals("task1", executionInstances.get(0).getProcessDefinitionActivityId());

        //signal task1
        this.executionCommandService.signal(executionInstances.get(0).getInstanceId(), null);

        //assert task2
        Assert.assertEquals(1, session.getProcessInstances().values().size());
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
                executionInstances = this.executionQueryService.findActiveExecutionList(instance.getInstanceId());
                Assert.assertEquals("task2", executionInstances.get(0).getProcessDefinitionActivityId());
            }
        }

        //jumpFrom task1
        this.executionCommandService.jumpFrom(executionInstances.get(0).getInstanceId(), "task1", null);

        //assert task1
        Assert.assertEquals(1, session.getProcessInstances().values().size());

        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
                executionInstances = this.executionQueryService.findActiveExecutionList(instance.getInstanceId());
                Assert.assertEquals("task1", executionInstances.get(0).getProcessDefinitionActivityId());
            }
        }

        //signal task1
        this.executionCommandService.signal(executionInstances.get(0).getInstanceId(), null);
        Assert.assertEquals(1, session.getProcessInstances().values().size());

        //assert task2
        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.running == instance.getStatus());
                executionInstances = this.executionQueryService.findActiveExecutionList(instance.getInstanceId());
                Assert.assertEquals("task2", executionInstances.get(0).getProcessDefinitionActivityId());
            }
        }

        //signal task2
        this.executionCommandService.signal(executionInstances.get(0).getInstanceId(), null);

        // assert end
        Assert.assertEquals(1, session.getProcessInstances().values().size());

        for (ProcessInstance instance : session.getProcessInstances().values()) {
            if (processInstanceId.equals(instance.getInstanceId())) {
                Assert.assertTrue(InstanceStatus.completed == instance.getStatus());
                executionInstances = this.executionQueryService.findActiveExecutionList(instance.getInstanceId());
                Assert.assertEquals(0, executionInstances.size());
            }
        }

    }

}
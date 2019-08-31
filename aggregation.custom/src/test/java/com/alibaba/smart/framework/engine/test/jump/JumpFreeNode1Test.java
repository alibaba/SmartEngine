package com.alibaba.smart.framework.engine.test.jump;

import java.util.HashMap;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shiyang.xsy
 * @date 2017/12/6
 */
public class JumpFreeNode1Test {
    ExecutionQueryService executionQueryService;
    ExecutionCommandService executionCommandService;
    private SmartEngine smartEngine;

    @Before
    public void before() {
        this.smartEngine = new DefaultSmartEngine();
        this.smartEngine.init(new DefaultProcessEngineConfiguration());
        this.executionQueryService = this.smartEngine.getExecutionQueryService();
        this.executionCommandService = this.smartEngine.getExecutionCommandService();
        this.deploy();
    }

    private void deploy() {
        RepositoryCommandService repositoryCommandService = this.smartEngine
            .getRepositoryCommandService();
        repositoryCommandService
            .deploy("smart-engine/jump1.bpmn20.xml");
    }

    @Test
    public void test() {
        PersisterSession.create();
        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

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

        //jump task1
        this.executionCommandService.jump(executionInstances.get(0).getInstanceId(), "task1", null);

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
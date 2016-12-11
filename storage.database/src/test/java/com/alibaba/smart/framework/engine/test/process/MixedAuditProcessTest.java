package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MixedAuditProcessTest {


    @Test
    public void testUserTaskExclusive() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setPersisteModel("mysql-tddl");

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");
        assertEquals(17, processDefinition.getProcess().getElements().size());

        ProcessCommandService processCommandService = smartEngine.getProcessService();

        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion()
                );

        Assert.assertNotNull(processInstance);


        List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();

        Assert.assertNotNull(activityInstances);

        int size = activityInstances.size();
        assertEquals(3, size);

        ActivityInstance lastActivityInstance = activityInstances.get(size - 1);
        assertEquals("theTask1", lastActivityInstance.getActivityId());


        ExecutionInstance lastExecutionInstance = lastActivityInstance.getExecutionInstance();
        Assert.assertNotNull(lastExecutionInstance);

        assertEquals(processInstance.getInstanceId(), lastExecutionInstance.getProcessInstanceId());
        assertEquals(lastActivityInstance.getInstanceId(), lastExecutionInstance.getActivityInstanceId());
        assertEquals(lastActivityInstance.getActivityId(), lastExecutionInstance.getActivityId());

        TaskInstance latestTaskInstance = lastExecutionInstance.getTaskInstance();
        assertNotNull(latestTaskInstance);
        assertEquals("theTask1", latestTaskInstance.getActivityId());


        //1st: create 1st task
        TaskCommandService taskCommandService = smartEngine.getTaskCommandService();

        TaskInstance taskInstance = latestTaskInstance;




    }


}
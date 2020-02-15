package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.simulation.ProcessSimulation;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProcessSimulationTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {



        ProcessSimulation processSimulation = new ProcessSimulation(smartEngine);

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("simple-process-simulation.bpmn.xml").getFirstProcessDefinition();
        assertEquals(14, processDefinition.getBaseElementList().size());

        //List<Activity> activitieList = null;

        List<Activity> activitieList = processSimulation.simulateOutcomingActivities(processDefinition.getId(), processDefinition.getVersion(),
            "receiveTask0", null);
        Assert.assertNotNull(activitieList);
        assertEquals(1, activitieList.size());
        assertEquals("receiveTask1", activitieList.get(0).getId());

        //activitieList =  processSimulation.simulateOutcomingActivities("processSimulation","1.0.0","receiveTask1",
        // null);
        //Assert.assertNotNull(activitieList);
        //Assert.assertEquals(0,activitieList.size());

        Map<String, Object> simulationContext = new HashMap<String, Object>(2);
        simulationContext.put("route", "a");
        activitieList = processSimulation.simulateOutcomingActivities("Process_1", "1.0.0", "receiveTask1",
            simulationContext);
        Assert.assertNotNull(activitieList);
        assertEquals(1, activitieList.size());
        assertEquals("receiveTask_a", activitieList.get(0).getId());

        simulationContext = new HashMap<String, Object>(2);
        simulationContext.put("route", "b");
        activitieList = processSimulation.simulateOutcomingActivities("Process_1", "1.0.0", "receiveTask1",
            simulationContext);
        Assert.assertNotNull(activitieList);
        assertEquals(1, activitieList.size());
        assertEquals("receiveTask_b", activitieList.get(0).getId());

    }

}
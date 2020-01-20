package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 November  17:54.
 */
public class CustomPropertiesTest extends CustomBaseTestCase {

    @Test
    public void test() throws Exception {



        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("custom-properties.bpmn.xml").getFirstProcessDefinition();
        assertEquals(9, processDefinition.getBaseElementList().size());

        processDefinition = repositoryQueryService.getCachedProcessDefinition(processDefinition.getId(), processDefinition.getVersion());
        assertEquals(9, processDefinition.getBaseElementList().size());

        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ProcessInstance processInstance = processCommandService.start(processDefinition.getId(),
            processDefinition.getVersion());
        processCommandService.abort(processInstance.getInstanceId());

    }


}
package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTaskExtensionTest extends CustomBaseTestCase {

    @Test
    public void test() throws Exception {
        BasicServiceTaskDelegation.resetCounter();


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-extension-test.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        ExtensionElementContainer idBasedElement = (ExtensionElementContainer) processDefinition.getIdBasedElementMap().get("user-task-1589789116294");
        Object executionListener = idBasedElement.getExtensionElements().getDecorationMap().get("ExecutionListener");
        Assert.assertNotNull(executionListener);


    }



}
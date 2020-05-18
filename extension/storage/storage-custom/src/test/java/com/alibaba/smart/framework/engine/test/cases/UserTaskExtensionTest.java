package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
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

        ExtensionBasedElement idBasedElement = (ExtensionBasedElement) processDefinition.getIdBasedElementMap().get("user-task-1589789116294");
        Object executionListener = idBasedElement.getExtensionElements().getDecorationMap().get("ExecutionListener");
        Assert.assertNotNull(executionListener);


    }



}
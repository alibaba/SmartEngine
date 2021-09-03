package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;
import com.alibaba.smart.framework.engine.test.delegation.ExclusiveTaskDelegation;

import org.junit.Test;

public class AdvancedExclusvieGatewayProcessTest extends CommonTestCode {

    @Override
    public void setUp() {
        super.setUp();

        BasicServiceTaskDelegation.resetCounter();
        ExclusiveTaskDelegation.resetCounter();
    }

    @Test
    public void test() throws Exception {

        ExecutionInstance executionInstance = commonCodeSnippet("advanced-exclusviegateway-process.bpmn.xml");


        commonCode(null, executionInstance);

    }

}
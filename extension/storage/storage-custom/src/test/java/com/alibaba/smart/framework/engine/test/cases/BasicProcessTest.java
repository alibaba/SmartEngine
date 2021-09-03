package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;
import com.alibaba.smart.framework.engine.test.delegation.ExclusiveTaskDelegation;

import org.junit.Test;
public class BasicProcessTest extends CommonTestCode {

    @Override
    public void setUp() {
        super.setUp();

        BasicServiceTaskDelegation.resetCounter();
        ExclusiveTaskDelegation.resetCounter();
    }

    @Test
    public void test() throws Exception {

        ExecutionInstance executionInstance = commonCodeSnippet("basic-process.bpmn.xml");

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("route", "a");

        commonCode(request, executionInstance);

    }




}
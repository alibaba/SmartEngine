package com.alibaba.smart.framework.engine.test.delegation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;

import lombok.Getter;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RightJavaDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(RightJavaDelegation.class);

    @Getter
    private static List<String> arrayList = new ArrayList<String>();

    @Override
    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();
        if (null != request) {
            Assert.assertEquals("right", request.get("value"));
        }

    }

}

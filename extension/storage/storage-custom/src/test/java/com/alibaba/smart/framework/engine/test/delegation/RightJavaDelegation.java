package com.alibaba.smart.framework.engine.test.delegation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

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

        String processDefinitionActivityId =  executionContext.getExecutionInstance().getProcessDefinitionActivityId();

        ExtensionElementContainer idBasedElement = (ExtensionElementContainer)executionContext.getProcessDefinition().getIdBasedElementMap().get(
            processDefinitionActivityId);

        ExtensionElements extensionElements = idBasedElement.getExtensionElements();

            Map map = (Map)extensionElements.getDecorationMap().get(
                ExtensionElementsConstant.PROPERTIES);

        Assert.assertEquals("right", map.get("value"));




    }

}

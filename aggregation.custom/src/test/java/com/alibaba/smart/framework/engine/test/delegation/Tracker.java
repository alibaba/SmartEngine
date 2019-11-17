package com.alibaba.smart.framework.engine.test.delegation;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.delegation.JavaDelegation;
import com.alibaba.smart.framework.engine.listener.EventListener;
import com.alibaba.smart.framework.engine.listener.EventListenerAggregation;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class Tracker implements JavaDelegation {
    @Override
    public void execute(ExecutionContext executionContext) {

        String processDefinitionActivityId =  executionContext.getExecutionInstance().getProcessDefinitionActivityId();

        ExtensionBasedElement idBasedElement = (ExtensionBasedElement)executionContext.getProcessDefinition().getIdBasedElementMap().get(
            processDefinitionActivityId);

        ExtensionElements extensionElements = idBasedElement.getExtensionElements();
        if(null != extensionElements){

            Map map = (Map)extensionElements.getDecorationMap().get(
                ExtensionElementsConstant.PROPERTIES);

            executionContext.getResponse().putAll(map);

        }

    }
}

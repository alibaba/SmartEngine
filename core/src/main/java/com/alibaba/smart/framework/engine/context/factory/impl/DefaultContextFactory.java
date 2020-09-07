package com.alibaba.smart.framework.engine.context.factory.impl;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ContextFactory.class)
public class DefaultContextFactory implements ContextFactory {

    @Override
    public ExecutionContext create() {
        return new DefaultInstanceContext();
    }

    @Override
    public ExecutionContext createFromParentContext(ExecutionContext parentContext) {

        ProcessEngineConfiguration processEngineConfiguration = parentContext.getProcessEngineConfiguration();
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ProcessInstance processInstance = parentContext.getProcessInstance();

        ExecutionContext subContext = this.create();
        subContext.setParent(parentContext);

        subContext.setProcessEngineConfiguration(processEngineConfiguration);
        subContext.setRequest(parentContext.getRequest());

        ProcessDefinition pvmProcessDefinition = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class).getProcessDefinition(processInstance.getProcessDefinitionId(), processInstance.getProcessDefinitionVersion());

        subContext.setProcessDefinition(pvmProcessDefinition);
        subContext.setProcessInstance(processInstance);

        return subContext;
    }
}

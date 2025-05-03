package com.alibaba.smart.framework.engine.context.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ContextFactory.class)
public class DefaultContextFactory implements ContextFactory {

    @Override
    public ExecutionContext createSignalContext(Map<String, Object> request,
                                                ProcessEngineConfiguration processEngineConfiguration,
                                                ExecutionInstance executionInstance,
                                                ActivityInstance activityInstance,
                                                ProcessInstance processInstance,
                                                ProcessDefinition processDefinition) {
        ExecutionContext executionContext = new DefaultExecutionContext();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setProcessDefinition(processDefinition);
        executionContext.setProcessInstance(processInstance);
        executionContext.setExecutionInstance(executionInstance);


        if(null != executionInstance){

            executionContext.setBlockId(executionInstance.getBlockId());


            String processDefinitionActivityId = executionInstance.getProcessDefinitionActivityId();
            Map<String, IdBasedElement> idBasedElementMap = processDefinition.getIdBasedElementMap();
            IdBasedElement idBasedElement = idBasedElementMap.get(
                processDefinitionActivityId);
            executionContext.setBaseElement(idBasedElement);

        }

        executionContext.setActivityInstance(activityInstance);
        executionContext.setRequest(request);
        return executionContext;
    }

    @Override
    public ExecutionContext createGatewayContext(ExecutionContext parentContext) {

        ProcessInstance processInstance = parentContext.getProcessInstance();

        Map<String, Object> request = parentContext.getRequest();
        Map<String, Object> response = parentContext.getResponse();

        ExecutionContext subContext = createProcessContext(parentContext.getProcessEngineConfiguration(), processInstance, request, response,parentContext);

        subContext.setExecutionInstance(subContext.getExecutionInstance());

        if(null != parentContext.getExecutionInstance()){
            subContext.setBlockId(parentContext.getExecutionInstance().getInstanceId());
        }

        subContext.setBaseElement(parentContext.getBaseElement());
        subContext.setActivityInstance(parentContext.getActivityInstance());

        subContext.setInnerExtra(parentContext.getInnerExtra());

        return subContext;
    }

    @Override
    public ExecutionContext createProcessContext(ProcessEngineConfiguration processEngineConfiguration,
                                                 ProcessInstance processInstance, Map<String, Object> request,
                                                 Map<String, Object> response, ExecutionContext mayBeNullParentContext) {

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        ProcessDefinitionContainer processDefinitionContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class);

        ExecutionContext subContext = new DefaultExecutionContext();

        String processDefinitionId = processInstance.getProcessDefinitionId();
        String processDefinitionVersion = processInstance.getProcessDefinitionVersion();

        ProcessDefinition processDefinition = processDefinitionContainer.getProcessDefinition(processDefinitionId,
            processDefinitionVersion);

        if(null == processDefinition){
            throw new EngineException("No ProcessDefinition found for processDefinitionId : "+processDefinitionId+",processDefinitionVersion : " +processDefinitionVersion);
        }


        subContext.setProcessDefinition(processDefinition);
        subContext.setProcessEngineConfiguration(processEngineConfiguration);
        subContext.setRequest(request);
        subContext.setResponse(response);
        subContext.setProcessInstance(processInstance);
        subContext.setParent(mayBeNullParentContext);



        return subContext;
    }


}

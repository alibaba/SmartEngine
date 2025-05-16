package com.alibaba.smart.framework.engine.context.factory.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
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
import com.alibaba.smart.framework.engine.util.ObjectUtil;

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

        //设置租户ID：processDefinition>processInstance>activityInstance
        if (executionContext != null && executionContext.getTenantId() == null
                && processDefinition != null && processDefinition.getTenantId() != null) {
            executionContext.setTenantId(processDefinition.getTenantId());
        }

        if (executionContext != null && executionContext.getTenantId() == null
                && processInstance != null && processInstance.getTenantId() != null) {
            executionContext.setTenantId(processInstance.getTenantId());
        }

        if (executionContext != null && executionContext.getTenantId() == null
                && activityInstance != null && activityInstance.getTenantId() != null) {
            executionContext.setTenantId(activityInstance.getTenantId());
        }

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

        subContext.setTenantId(parentContext.getTenantId());

        return subContext;
    }

    @Override
    public ExecutionContext createProcessContext(ProcessEngineConfiguration processEngineConfiguration,
                                                 ProcessInstance processInstance, Map<String, Object> request,
                                                 Map<String, Object> response, ExecutionContext mayBeNullParentContext) {

        String tenantId = null;
        if(null != request) {
            tenantId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        ProcessDefinitionContainer processDefinitionContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class);

        ExecutionContext subContext = new DefaultExecutionContext();

        String processDefinitionId = processInstance.getProcessDefinitionId();
        String processDefinitionVersion = processInstance.getProcessDefinitionVersion();

        ProcessDefinition processDefinition = processDefinitionContainer.getProcessDefinition(processDefinitionId,
            processDefinitionVersion,tenantId);

        if(null == processDefinition){
            throw new EngineException("No ProcessDefinition found for processDefinitionId : "+processDefinitionId+",processDefinitionVersion : " +processDefinitionVersion);
        }

        subContext.setProcessDefinition(processDefinition);
        subContext.setProcessEngineConfiguration(processEngineConfiguration);
        subContext.setRequest(request);
        subContext.setResponse(response);
        subContext.setProcessInstance(processInstance);
        subContext.setParent(mayBeNullParentContext);
        subContext.setTenantId(tenantId);

        return subContext;
    }
}

package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.configuration.scanner.ExtensionBindingResult;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.VariableCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.NotificationQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;
import com.alibaba.smart.framework.engine.service.query.SupervisionQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;
import com.alibaba.smart.framework.engine.query.NotificationQuery;
import com.alibaba.smart.framework.engine.query.ProcessInstanceQuery;
import com.alibaba.smart.framework.engine.query.SupervisionQuery;
import com.alibaba.smart.framework.engine.query.TaskQuery;
import com.alibaba.smart.framework.engine.query.impl.NotificationQueryImpl;
import com.alibaba.smart.framework.engine.query.impl.ProcessInstanceQueryImpl;
import com.alibaba.smart.framework.engine.query.impl.SupervisionQueryImpl;
import com.alibaba.smart.framework.engine.query.impl.TaskQueryImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultSmartEngine implements SmartEngine {


    @Getter
    @Setter
    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void init(ProcessEngineConfiguration processEngineConfiguration) {
        this.setProcessEngineConfiguration(processEngineConfiguration);
        processEngineConfiguration.setSmartEngine(this);

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        annotationScanner.scan(processEngineConfiguration,  ExtensionBinding.class);

        Map<String, ExtensionBindingResult> scanResult = annotationScanner.getScanResult();

        lifeCycleStarted(scanResult);

    }

    protected void lifeCycleStarted(Map<String, ExtensionBindingResult> scanResult) {
        for (Entry<String, ExtensionBindingResult> stringExtensionBindingResultEntry : scanResult.entrySet()) {
            ExtensionBindingResult bindingResult = stringExtensionBindingResultEntry.getValue();
            Map<Class, Object> bindingMap = bindingResult.getBindingMap();
            for (Entry<Class, Object> classObjectEntry : bindingMap.entrySet()) {
                Object value = classObjectEntry.getValue();
                if( value instanceof LifeCycleHook){
                    ((LifeCycleHook)value).start();
                }
            }

        }
    }

    @Override
    public void destroy() {

    }


    @Override
    public RepositoryCommandService getRepositoryCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,RepositoryCommandService.class);

    }

    @Override
    public RepositoryQueryService getRepositoryQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,RepositoryQueryService.class);
    }

    @Override
    public DeploymentCommandService getDeploymentCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,DeploymentCommandService.class);
    }

    @Override
    public ProcessCommandService getProcessCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,ProcessCommandService.class);
    }

    @Override
    public ExecutionCommandService getExecutionCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,ExecutionCommandService.class);
    }

    @Override
    public TaskCommandService getTaskCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,TaskCommandService.class);
    }

    @Override
    public VariableCommandService getVariableCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,VariableCommandService.class);
    }

    @Override
    public DeploymentQueryService getDeploymentQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,DeploymentQueryService.class);
    }

    @Override
    public ProcessQueryService getProcessQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,ProcessQueryService.class);
    }

    @Override
    public ActivityQueryService getActivityQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,ActivityQueryService.class);
    }

    @Override
    public ExecutionQueryService getExecutionQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,ExecutionQueryService.class);
    }

    @Override
    public TaskQueryService getTaskQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,TaskQueryService.class);
    }

    @Override
    public VariableQueryService getVariableQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,VariableQueryService.class);
    }

    @Override
    public TaskAssigneeQueryService getTaskAssigneeQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,TaskAssigneeQueryService.class);
    }

    @Override
    public SupervisionCommandService getSupervisionCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE, SupervisionCommandService.class);
    }

    @Override
    public SupervisionQueryService getSupervisionQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE, SupervisionQueryService.class);
    }

    @Override
    public NotificationCommandService getNotificationCommandService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE, NotificationCommandService.class);
    }

    @Override
    public NotificationQueryService getNotificationQueryService() {
        return processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE, NotificationQueryService.class);
    }

    // ============ Fluent Query API ============

    @Override
    public TaskQuery createTaskQuery() {
        return new TaskQueryImpl(processEngineConfiguration);
    }

    @Override
    public ProcessInstanceQuery createProcessQuery() {
        return new ProcessInstanceQueryImpl(processEngineConfiguration);
    }

    @Override
    public SupervisionQuery createSupervisionQuery() {
        return new SupervisionQueryImpl(processEngineConfiguration);
    }

    @Override
    public NotificationQuery createNotificationQuery() {
        return new NotificationQueryImpl(processEngineConfiguration);
    }

}

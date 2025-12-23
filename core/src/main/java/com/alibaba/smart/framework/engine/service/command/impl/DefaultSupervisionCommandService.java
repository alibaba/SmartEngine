package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.SupervisionConstant;
import com.alibaba.smart.framework.engine.exception.SupervisionException;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultSupervisionInstance;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;

/**
 * 督办命令服务默认实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = SupervisionCommandService.class)
public class DefaultSupervisionCommandService implements SupervisionCommandService, LifeCycleHook, ProcessEngineConfigurationAware {

    private ProcessEngineConfiguration processEngineConfiguration;
    private SupervisionInstanceStorage supervisionInstanceStorage;

    @Override
    public void start() {
        AnnotationScanner annotationScanner = this.processEngineConfiguration.getAnnotationScanner();
        this.supervisionInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, SupervisionInstanceStorage.class);
    }

    @Override
    public void stop() {
        // 清理资源
    }

    @Override
    public SupervisionInstance createSupervision(String taskInstanceId, String supervisorUserId, 
                                                String reason, String supervisionType, String tenantId) {
        if (taskInstanceId == null || supervisorUserId == null) {
            throw new ValidationException("TaskInstanceId and SupervisorUserId cannot be null");
        }
        
        if (supervisionType == null || (!SupervisionConstant.SupervisionType.URGE.equals(supervisionType) 
                && !SupervisionConstant.SupervisionType.TRACK.equals(supervisionType) 
                && !SupervisionConstant.SupervisionType.REMIND.equals(supervisionType))) {
            throw new ValidationException("Invalid supervision type: " + supervisionType);
        }

        try {
            SupervisionInstance supervisionInstance = new DefaultSupervisionInstance();
            supervisionInstance.setTaskInstanceId(taskInstanceId);
            supervisionInstance.setSupervisorUserId(supervisorUserId);
            supervisionInstance.setSupervisionReason(reason);
            supervisionInstance.setSupervisionType(supervisionType);
            supervisionInstance.setStatus(SupervisionConstant.SupervisionStatus.ACTIVE);
            supervisionInstance.setTenantId(tenantId);
            
            // 设置ID生成器
            processEngineConfiguration.getIdGenerator().generate(supervisionInstance);
            
            return supervisionInstanceStorage.insert(supervisionInstance, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to create supervision", e);
        }
    }

    @Override
    public void closeSupervision(String supervisionId, String tenantId) {
        if (supervisionId == null) {
            throw new ValidationException("SupervisionId cannot be null");
        }

        try {
            supervisionInstanceStorage.closeSupervision(supervisionId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to close supervision: " + supervisionId, e);
        }
    }

    @Override
    public List<SupervisionInstance> batchCreateSupervision(List<String> taskInstanceIds, 
                                                           String supervisorUserId, String reason, 
                                                           String supervisionType, String tenantId) {
        if (taskInstanceIds == null || taskInstanceIds.isEmpty()) {
            throw new ValidationException("TaskInstanceIds cannot be null or empty");
        }

        List<SupervisionInstance> results = new ArrayList<>();
        for (String taskInstanceId : taskInstanceIds) {
            try {
                SupervisionInstance supervision = createSupervision(taskInstanceId, supervisorUserId, 
                                                                  reason, supervisionType, tenantId);
                results.add(supervision);
            } catch (Exception e) {
                throw new SupervisionException("Failed to create supervision for task: " + taskInstanceId, e);
            }
        }
        return results;
    }

    @Override
    public void autoCloseSupervisionByTask(String taskInstanceId, String tenantId) {
        if (taskInstanceId == null) {
            throw new ValidationException("TaskInstanceId cannot be null");
        }

        try {
            supervisionInstanceStorage.closeSupervisionByTask(taskInstanceId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to auto close supervision by task: " + taskInstanceId, e);
        }
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
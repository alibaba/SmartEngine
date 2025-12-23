package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.exception.SupervisionException;
import com.alibaba.smart.framework.engine.exception.ValidationException;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;
import com.alibaba.smart.framework.engine.service.query.SupervisionQueryService;

/**
 * 督办查询服务默认实现
 * 
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = SupervisionQueryService.class)
public class DefaultSupervisionQueryService implements SupervisionQueryService, LifeCycleHook, ProcessEngineConfigurationAware {

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
    public List<SupervisionInstance> findSupervisionList(SupervisionQueryParam param) {
        if (param == null) {
            throw new ValidationException("SupervisionQueryParam cannot be null");
        }

        try {
            return supervisionInstanceStorage.findSupervisionList(param, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to find supervision list", e);
        }
    }

    @Override
    public Long countSupervision(SupervisionQueryParam param) {
        if (param == null) {
            throw new ValidationException("SupervisionQueryParam cannot be null");
        }

        try {
            return supervisionInstanceStorage.countSupervision(param, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to count supervision", e);
        }
    }

    @Override
    public List<SupervisionInstance> findActiveSupervisionByTask(String taskInstanceId, String tenantId) {
        if (taskInstanceId == null) {
            throw new ValidationException("TaskInstanceId cannot be null");
        }

        try {
            return supervisionInstanceStorage.findActiveSupervisionByTask(taskInstanceId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to find active supervision by task: " + taskInstanceId, e);
        }
    }

    @Override
    public SupervisionInstance findOne(String supervisionId, String tenantId) {
        if (supervisionId == null) {
            throw new ValidationException("SupervisionId cannot be null");
        }

        try {
            return supervisionInstanceStorage.find(supervisionId, tenantId, processEngineConfiguration);
        } catch (Exception e) {
            throw new SupervisionException("Failed to find supervision: " + supervisionId, e);
        }
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
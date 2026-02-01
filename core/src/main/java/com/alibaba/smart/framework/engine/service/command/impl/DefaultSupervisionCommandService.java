package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;

/**
 * Default implementation of SupervisionCommandService.
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = SupervisionCommandService.class)
public class DefaultSupervisionCommandService implements SupervisionCommandService, LifeCycleHook,
        ProcessEngineConfigurationAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSupervisionCommandService.class);

    private ProcessEngineConfiguration processEngineConfiguration;
    private SupervisionInstanceStorage supervisionInstanceStorage;
    private TaskInstanceStorage taskInstanceStorage;
    private NotificationCommandService notificationCommandService;

    @Override
    public void start() {
        AnnotationScanner annotationScanner = this.processEngineConfiguration.getAnnotationScanner();
        this.supervisionInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
                SupervisionInstanceStorage.class);
        this.taskInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
                TaskInstanceStorage.class);
        this.notificationCommandService = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
                NotificationCommandService.class);
    }

    @Override
    public void stop() {
        // Clean up resources if needed
    }

    @Override
    public SupervisionInstance createSupervision(String taskInstanceId, String supervisorUserId,
                                                 String reason, String supervisionType, String tenantId) {
        // Validate required parameters
        if (taskInstanceId == null || supervisorUserId == null) {
            throw new ValidationException("TaskInstanceId and SupervisorUserId cannot be null");
        }

        if (!isValidSupervisionType(supervisionType)) {
            throw new ValidationException("Invalid supervision type: " + supervisionType);
        }

        LOGGER.info("Creating supervision for task: {}, supervisor: {}, type: {}",
                taskInstanceId, supervisorUserId, supervisionType);

        try {
            // First, get task instance to retrieve processInstanceId (required for NOT NULL constraint)
            TaskInstance taskInstance = taskInstanceStorage.find(taskInstanceId, tenantId, processEngineConfiguration);
            if (taskInstance == null) {
                throw new ValidationException("Task not found: " + taskInstanceId);
            }

            SupervisionInstance supervisionInstance = new DefaultSupervisionInstance();
            supervisionInstance.setTaskInstanceId(taskInstanceId);
            supervisionInstance.setProcessInstanceId(taskInstance.getProcessInstanceId()); // Set before insert
            supervisionInstance.setSupervisorUserId(supervisorUserId);
            supervisionInstance.setSupervisionReason(reason);
            supervisionInstance.setSupervisionType(supervisionType);
            supervisionInstance.setStatus(SupervisionConstant.SupervisionStatus.ACTIVE);
            supervisionInstance.setTenantId(tenantId);

            // Generate ID
            processEngineConfiguration.getIdGenerator().generate(supervisionInstance);

            // Save supervision record
            SupervisionInstance result = supervisionInstanceStorage.insert(supervisionInstance,
                    processEngineConfiguration);

            // Update task priority and send notification
            processTaskAfterSupervision(taskInstance, supervisorUserId, reason);

            LOGGER.info("Supervision created successfully: {}", result.getInstanceId());
            return result;

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to create supervision for task: {}", taskInstanceId, e);
            throw new SupervisionException("Failed to create supervision", e);
        }
    }

    /**
     * Process task after supervision is created: update priority and send notification.
     */
    private void processTaskAfterSupervision(TaskInstance taskInstance,
                                             String supervisorUserId, String reason) {
        if (taskInstance == null) {
            return;
        }

        String tenantId = taskInstance.getTenantId();

        // Increase task priority
        int newPriority = (taskInstance.getPriority() != null ? taskInstance.getPriority() : 0) + 1;
        taskInstance.setPriority(newPriority);
        taskInstanceStorage.update(taskInstance, processEngineConfiguration);
        LOGGER.debug("Task priority updated to {} for task: {}", newPriority, taskInstance.getInstanceId());

        // Send supervision notification to task assignee
        if (taskInstance.getClaimUserId() != null && notificationCommandService != null) {
            try {
                notificationCommandService.sendSingleNotification(
                        taskInstance.getProcessInstanceId(),
                        taskInstance.getInstanceId(),
                        supervisorUserId,
                        taskInstance.getClaimUserId(),
                        "任务督办通知",
                        "您的任务被督办，原因：" + reason,
                        "督办提醒",
                        tenantId
                );
                LOGGER.debug("Supervision notification sent to user: {}", taskInstance.getClaimUserId());
            } catch (Exception e) {
                LOGGER.warn("Failed to send supervision notification to user: {}", taskInstance.getClaimUserId(), e);
                // Don't throw - notification failure should not fail the supervision creation
            }
        }
    }

    @Override
    public void closeSupervision(String supervisionId, String tenantId) {
        if (supervisionId == null) {
            throw new ValidationException("SupervisionId cannot be null");
        }

        LOGGER.info("Closing supervision: {}", supervisionId);

        try {
            supervisionInstanceStorage.closeSupervision(supervisionId, tenantId, processEngineConfiguration);
            LOGGER.info("Supervision closed successfully: {}", supervisionId);
        } catch (Exception e) {
            LOGGER.error("Failed to close supervision: {}", supervisionId, e);
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

        LOGGER.info("Batch creating {} supervisions for supervisor: {}", taskInstanceIds.size(), supervisorUserId);

        List<SupervisionInstance> results = new ArrayList<>();
        for (String taskInstanceId : taskInstanceIds) {
            try {
                SupervisionInstance supervision = createSupervision(taskInstanceId, supervisorUserId,
                        reason, supervisionType, tenantId);
                results.add(supervision);
            } catch (Exception e) {
                LOGGER.error("Failed to create supervision for task: {}", taskInstanceId, e);
                throw new SupervisionException("Failed to create supervision for task: " + taskInstanceId, e);
            }
        }

        LOGGER.info("Batch supervision creation completed: {} created", results.size());
        return results;
    }

    @Override
    public void autoCloseSupervisionByTask(String taskInstanceId, String tenantId) {
        if (taskInstanceId == null) {
            throw new ValidationException("TaskInstanceId cannot be null");
        }

        LOGGER.info("Auto closing supervisions for task: {}", taskInstanceId);

        try {
            supervisionInstanceStorage.closeSupervisionByTask(taskInstanceId, tenantId, processEngineConfiguration);
            LOGGER.info("Supervisions auto closed for task: {}", taskInstanceId);
        } catch (Exception e) {
            LOGGER.error("Failed to auto close supervisions for task: {}", taskInstanceId, e);
            throw new SupervisionException("Failed to auto close supervision by task: " + taskInstanceId, e);
        }
    }

    /**
     * Check if supervision type is valid.
     */
    private boolean isValidSupervisionType(String supervisionType) {
        return supervisionType != null && (
                SupervisionConstant.SupervisionType.URGE.equals(supervisionType)
                        || SupervisionConstant.SupervisionType.TRACK.equals(supervisionType)
                        || SupervisionConstant.SupervisionType.REMIND.equals(supervisionType)
        );
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}

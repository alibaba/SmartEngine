package com.alibaba.smart.framework.engine.configuration;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

/**
 * SPI for publishing task lifecycle events.
 * Platform implementations bridge these events to their own event bus (e.g. Spring ApplicationEvent).
 *
 * Similar to {@link TaskAssigneeDispatcher}, this is set on {@link ProcessEngineConfiguration}
 * and invoked by the engine at task state change points.
 *
 * @since 3.7.0
 */
public interface TaskEventPublisher {

    /**
     * Publish a task lifecycle event.
     *
     * @param event     the event type (TASK_ASSIGNED, TASK_COMPLETED, etc.)
     * @param taskInstance the task instance involved
     * @param tenantId  tenant identifier
     * @param extra     additional context (e.g. assignees, fromUserId, toUserId, reason)
     */
    void publish(EventConstant event, TaskInstance taskInstance,
                 String tenantId, Map<String, Object> extra);
}

package com.alibaba.smart.framework.engine.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

/**
 * Fluent query interface for TaskInstance.
 * Provides type-safe method chaining for building task queries.
 *
 * <p>Example usage:
 * <pre>{@code
 * List<TaskInstance> tasks = smartEngine.createTaskQuery()
 *     .processInstanceId("12345")
 *     .taskAssignee("user001")
 *     .taskStatus(TaskInstanceConstant.PENDING)
 *     .orderByCreateTime().desc()
 *     .listPage(0, 10);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public interface TaskQuery extends Query<TaskQuery, TaskInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by task instance ID.
     *
     * @param taskInstanceId the task instance ID
     * @return this query for method chaining
     */
    TaskQuery taskInstanceId(String taskInstanceId);

    /**
     * Filter by process instance ID.
     *
     * @param processInstanceId the process instance ID
     * @return this query for method chaining
     */
    TaskQuery processInstanceId(String processInstanceId);

    /**
     * Filter by multiple process instance IDs.
     *
     * @param processInstanceIds the list of process instance IDs
     * @return this query for method chaining
     */
    TaskQuery processInstanceIdIn(List<String> processInstanceIds);

    /**
     * Filter by activity instance ID.
     *
     * @param activityInstanceId the activity instance ID
     * @return this query for method chaining
     */
    TaskQuery activityInstanceId(String activityInstanceId);

    /**
     * Filter by process definition type.
     *
     * @param processDefinitionType the process definition type
     * @return this query for method chaining
     */
    TaskQuery processDefinitionType(String processDefinitionType);

    /**
     * Filter by process definition activity ID.
     *
     * @param processDefinitionActivityId the process definition activity ID
     * @return this query for method chaining
     */
    TaskQuery processDefinitionActivityId(String processDefinitionActivityId);

    /**
     * Filter by task status.
     *
     * @param status the task status
     * @return this query for method chaining
     * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
     */
    TaskQuery taskStatus(String status);

    /**
     * Filter by claim user ID (task assignee).
     *
     * @param claimUserId the user ID who claimed the task
     * @return this query for method chaining
     */
    TaskQuery taskAssignee(String claimUserId);

    /**
     * Filter by tag.
     *
     * @param tag the task tag
     * @return this query for method chaining
     */
    TaskQuery taskTag(String tag);

    /**
     * Filter by extension field.
     *
     * @param extension the extension value
     * @return this query for method chaining
     */
    TaskQuery taskExtension(String extension);

    /**
     * Filter by priority.
     *
     * @param priority the task priority
     * @return this query for method chaining
     */
    TaskQuery taskPriority(Integer priority);

    /**
     * Filter by comment.
     *
     * @param comment the task comment
     * @return this query for method chaining
     */
    TaskQuery taskComment(String comment);

    /**
     * Filter by title.
     *
     * @param title the task title
     * @return this query for method chaining
     */
    TaskQuery taskTitle(String title);

    /**
     * Filter by complete time range (start).
     *
     * @param completeTimeStart the start of complete time range
     * @return this query for method chaining
     */
    TaskQuery completeTimeAfter(Date completeTimeStart);

    /**
     * Filter by complete time range (end).
     *
     * @param completeTimeEnd the end of complete time range
     * @return this query for method chaining
     */
    TaskQuery completeTimeBefore(Date completeTimeEnd);

    // ============ Ordering ============

    /**
     * Order by task instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByTaskId();

    /**
     * Order by create time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByCreateTime();

    /**
     * Order by modify time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByModifyTime();

    /**
     * Order by claim time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByClaimTime();

    /**
     * Order by complete time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByCompleteTime();

    /**
     * Order by priority.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByPriority();

    /**
     * Set ascending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    TaskQuery asc();

    /**
     * Set descending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    TaskQuery desc();
}

package com.alibaba.smart.framework.engine.query;

import java.util.Arrays;
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
public interface TaskQuery extends ProcessBoundQuery<TaskQuery, TaskInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by task instance ID.
     *
     * @param taskInstanceId the task instance ID
     * @return this query for method chaining
     */
    TaskQuery taskInstanceId(String taskInstanceId);

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
     * Conditionally filter by process definition type.
     *
     * @param condition             if true, the filter is applied
     * @param processDefinitionType the process definition type
     * @return this query for method chaining
     */
    TaskQuery processDefinitionType(boolean condition, String processDefinitionType);

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
     * Conditionally filter by task status.
     *
     * @param condition if true, the filter is applied
     * @param status    the task status
     * @return this query for method chaining
     */
    TaskQuery taskStatus(boolean condition, String status);

    /**
     * Filter by multiple task statuses.
     *
     * @param statuses the list of task statuses
     * @return this query for method chaining
     */
    TaskQuery taskStatusIn(List<String> statuses);

    /**
     * Filter by multiple task statuses (varargs).
     *
     * @param statuses the task statuses
     * @return this query for method chaining
     */
    TaskQuery taskStatusIn(String... statuses);

    /**
     * Filter by claim user ID (task assignee).
     *
     * @param claimUserId the user ID who claimed the task
     * @return this query for method chaining
     */
    TaskQuery taskAssignee(String claimUserId);

    /**
     * Conditionally filter by claim user ID (task assignee).
     *
     * @param condition   if true, the filter is applied
     * @param claimUserId the user ID who claimed the task
     * @return this query for method chaining
     */
    TaskQuery taskAssignee(boolean condition, String claimUserId);

    /**
     * Filter by candidate user ID (via assignee table JOIN).
     * Finds tasks where the specified user is a candidate assignee (assignee_type = 'user').
     *
     * <p>This differs from {@link #taskAssignee(String)} which filters by the already-claimed user.
     * This method queries the task assignee candidate table to find tasks assigned to the user.
     *
     * @param userId the candidate user ID
     * @return this query for method chaining
     */
    TaskQuery taskCandidateUser(String userId);

    /**
     * Filter by candidate group ID (via assignee table JOIN).
     * Finds tasks where the specified group is a candidate assignee (assignee_type = 'group').
     *
     * @param groupId the candidate group ID
     * @return this query for method chaining
     */
    TaskQuery taskCandidateGroup(String groupId);

    /**
     * Filter by multiple candidate group IDs (via assignee table JOIN).
     * Finds tasks where any of the specified groups is a candidate assignee.
     *
     * @param groupIds the candidate group ID list
     * @return this query for method chaining
     */
    TaskQuery taskCandidateGroupIn(List<String> groupIds);

    /**
     * Filter by multiple candidate group IDs (varargs).
     *
     * @param groupIds the candidate group IDs
     * @return this query for method chaining
     */
    default TaskQuery taskCandidateGroupIn(String... groupIds) {
        return taskCandidateGroupIn(Arrays.asList(groupIds));
    }

    /**
     * Filter by candidate user OR candidate groups (via assignee table JOIN).
     * Finds tasks where the user is a candidate (assignee_type = 'user')
     * OR any of the groups is a candidate (assignee_type = 'group').
     *
     * <p>This is the most common pattern for pending task queries:
     * <pre>{@code
     * List<TaskInstance> myTasks = smartEngine.createTaskQuery()
     *     .taskCandidateOrGroup("user001", Arrays.asList("dept-A", "role-manager"))
     *     .taskStatus(TaskInstanceConstant.PENDING)
     *     .listPage(0, 10);
     * }</pre>
     *
     * @param userId   the candidate user ID
     * @param groupIds the candidate group ID list
     * @return this query for method chaining
     */
    TaskQuery taskCandidateOrGroup(String userId, List<String> groupIds);

    /**
     * Filter by tag.
     *
     * @param tag the task tag
     * @return this query for method chaining
     */
    TaskQuery taskTag(String tag);

    /**
     * Conditionally filter by tag.
     *
     * @param condition if true, the filter is applied
     * @param tag       the task tag
     * @return this query for method chaining
     */
    TaskQuery taskTag(boolean condition, String tag);

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
     * Conditionally filter by title.
     *
     * @param condition if true, the filter is applied
     * @param title     the task title
     * @return this query for method chaining
     */
    TaskQuery taskTitle(boolean condition, String title);

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

    /**
     * Filter by create time range (start, inclusive).
     *
     * @param createTimeStart tasks created at or after this time
     * @return this query for method chaining
     */
    TaskQuery createdAfter(Date createTimeStart);

    /**
     * Filter by create time range (end, exclusive).
     *
     * @param createTimeEnd tasks created before this time
     * @return this query for method chaining
     */
    TaskQuery createdBefore(Date createTimeEnd);

    /**
     * Filter by title using fuzzy match (LIKE %titleLike%).
     *
     * @param titleLike the title pattern to match
     * @return this query for method chaining
     */
    TaskQuery taskTitleLike(String titleLike);

    /**
     * Conditionally filter by title using fuzzy match.
     *
     * @param condition if true, the filter is applied
     * @param titleLike the title pattern to match
     * @return this query for method chaining
     */
    TaskQuery taskTitleLike(boolean condition, String titleLike);

    /**
     * Filter for unassigned tasks (no claim user).
     *
     * @return this query for method chaining
     */
    TaskQuery taskUnassigned();

    /**
     * Filter by multiple process definition types.
     *
     * @param types the list of process definition types
     * @return this query for method chaining
     */
    TaskQuery processDefinitionTypeIn(List<String> types);

    /**
     * Filter by multiple process definition types (varargs).
     *
     * @param types the process definition types
     * @return this query for method chaining
     */
    default TaskQuery processDefinitionTypeIn(String... types) {
        return processDefinitionTypeIn(Arrays.asList(types));
    }

    /**
     * Filter by assignee using fuzzy match (LIKE %claimUserIdLike%).
     *
     * @param claimUserIdLike the assignee pattern to match
     * @return this query for method chaining
     */
    TaskQuery taskAssigneeLike(String claimUserIdLike);

    /**
     * Filter by minimum priority (inclusive).
     *
     * @param minPriority the minimum priority value
     * @return this query for method chaining
     */
    TaskQuery taskMinPriority(Integer minPriority);

    /**
     * Filter by maximum priority (inclusive).
     *
     * @param maxPriority the maximum priority value
     * @return this query for method chaining
     */
    TaskQuery taskMaxPriority(Integer maxPriority);

    // ============ Domain code filters ============

    /**
     * Filter by domain code (exact match).
     *
     * @param domainCode the domain code
     * @return this query for method chaining
     */
    TaskQuery domainCode(String domainCode);

    /**
     * Conditionally filter by domain code.
     *
     * @param condition  if true, the filter is applied
     * @param domainCode the domain code
     * @return this query for method chaining
     */
    TaskQuery domainCode(boolean condition, String domainCode);

    /**
     * Filter by multiple domain codes (IN clause).
     *
     * @param domainCodes the list of domain codes
     * @return this query for method chaining
     */
    TaskQuery domainCodeIn(List<String> domainCodes);

    /**
     * Filter by multiple domain codes (varargs).
     *
     * @param domainCodes the domain codes
     * @return this query for method chaining
     */
    default TaskQuery domainCodeIn(String... domainCodes) {
        return domainCodeIn(Arrays.asList(domainCodes));
    }

    /**
     * Filter by domain code using fuzzy match (LIKE %domainCodeLike%).
     *
     * @param domainCodeLike the domain code pattern to match
     * @return this query for method chaining
     */
    TaskQuery domainCodeLike(String domainCodeLike);

    // ============ Extra JSON filters ============

    /**
     * Filter by a JSON key-value in the extra field (exact match).
     *
     * @param key   JSON key (single-level, e.g., "category")
     * @param value expected value
     * @return this query for method chaining
     */
    TaskQuery extraJson(String key, String value);

    /**
     * Filter by JSON key matching any of the given values (IN clause).
     *
     * @param key    JSON key (single-level)
     * @param values the list of expected values
     * @return this query for method chaining
     */
    TaskQuery extraJsonIn(String key, List<String> values);

    /**
     * Filter by JSON key matching any of the given values (varargs).
     *
     * @param key    JSON key (single-level)
     * @param values the expected values
     * @return this query for method chaining
     */
    default TaskQuery extraJsonIn(String key, String... values) {
        return extraJsonIn(key, Arrays.asList(values));
    }

    /**
     * Filter by JSON key using LIKE match.
     *
     * @param key     JSON key (single-level)
     * @param pattern the LIKE pattern
     * @return this query for method chaining
     */
    TaskQuery extraJsonLike(String key, String pattern);

    // ============ Ordering ============

    /**
     * Order by task instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    TaskQuery orderByTaskId();

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
}

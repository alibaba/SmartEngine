package com.alibaba.smart.framework.engine.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * Fluent query interface for ProcessInstance.
 * Provides type-safe method chaining for building process instance queries.
 *
 * <p>Example usage:
 * <pre>{@code
 * List<ProcessInstance> processes = smartEngine.createProcessQuery()
 *     .processDefinitionType("approval")
 *     .startedBy("user001")
 *     .processStatus(InstanceStatus.running)
 *     .orderByStartTime().desc()
 *     .listPage(0, 10);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public interface ProcessInstanceQuery extends Query<ProcessInstanceQuery, ProcessInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by process instance ID.
     *
     * @param processInstanceId the process instance ID
     * @return this query for method chaining
     */
    ProcessInstanceQuery processInstanceId(String processInstanceId);

    /**
     * Filter by multiple process instance IDs.
     *
     * @param processInstanceIds the list of process instance IDs
     * @return this query for method chaining
     */
    ProcessInstanceQuery processInstanceIdIn(List<String> processInstanceIds);

    /**
     * Filter by start user ID.
     *
     * @param startUserId the user ID who started the process
     * @return this query for method chaining
     */
    ProcessInstanceQuery startedBy(String startUserId);

    /**
     * Filter by process status.
     *
     * @param status the process status (use InstanceStatus enum value name)
     * @return this query for method chaining
     */
    ProcessInstanceQuery processStatus(String status);

    /**
     * Filter by process definition type.
     *
     * @param processDefinitionType the process definition type
     * @return this query for method chaining
     */
    ProcessInstanceQuery processDefinitionType(String processDefinitionType);

    /**
     * Filter by process definition ID and version.
     *
     * @param processDefinitionIdAndVersion the process definition ID and version
     * @return this query for method chaining
     */
    ProcessInstanceQuery processDefinitionIdAndVersion(String processDefinitionIdAndVersion);

    /**
     * Filter by parent instance ID.
     *
     * @param parentInstanceId the parent process instance ID
     * @return this query for method chaining
     */
    ProcessInstanceQuery parentInstanceId(String parentInstanceId);

    /**
     * Filter by business unique ID.
     *
     * @param bizUniqueId the business unique ID
     * @return this query for method chaining
     */
    ProcessInstanceQuery bizUniqueId(String bizUniqueId);

    /**
     * Filter by start time range (start).
     *
     * @param startTimeAfter the start of time range
     * @return this query for method chaining
     */
    ProcessInstanceQuery startedAfter(Date startTimeAfter);

    /**
     * Filter by start time range (end).
     *
     * @param startTimeBefore the end of time range
     * @return this query for method chaining
     */
    ProcessInstanceQuery startedBefore(Date startTimeBefore);

    /**
     * Filter by complete time range (start).
     *
     * @param completeTimeStart the start of complete time range
     * @return this query for method chaining
     */
    ProcessInstanceQuery completedAfter(Date completeTimeStart);

    /**
     * Filter by complete time range (end).
     *
     * @param completeTimeEnd the end of complete time range
     * @return this query for method chaining
     */
    ProcessInstanceQuery completedBefore(Date completeTimeEnd);

    // ============ Ordering ============

    /**
     * Order by process instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    ProcessInstanceQuery orderByProcessInstanceId();

    /**
     * Order by start time (create time).
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    ProcessInstanceQuery orderByStartTime();

    /**
     * Order by modify time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    ProcessInstanceQuery orderByModifyTime();

    /**
     * Order by complete time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    ProcessInstanceQuery orderByCompleteTime();

    /**
     * Set ascending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    ProcessInstanceQuery asc();

    /**
     * Set descending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    ProcessInstanceQuery desc();
}

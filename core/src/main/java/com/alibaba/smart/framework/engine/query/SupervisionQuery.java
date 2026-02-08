package com.alibaba.smart.framework.engine.query;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;

/**
 * Fluent query interface for SupervisionInstance.
 * Provides type-safe method chaining for building supervision queries.
 *
 * <p>Example usage:
 * <pre>{@code
 * List<SupervisionInstance> supervisions = smartEngine.createSupervisionQuery()
 *     .supervisorUserId("supervisor001")
 *     .supervisionStatus("active")
 *     .orderByCreateTime().desc()
 *     .listPage(0, 10);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public interface SupervisionQuery extends ProcessBoundQuery<SupervisionQuery, SupervisionInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by supervision instance ID.
     *
     * @param supervisionId the supervision instance ID
     * @return this query for method chaining
     */
    SupervisionQuery supervisionId(String supervisionId);

    /**
     * Filter by supervisor user ID.
     *
     * @param supervisorUserId the supervisor user ID
     * @return this query for method chaining
     */
    SupervisionQuery supervisorUserId(String supervisorUserId);

    /**
     * Conditionally filter by supervisor user ID.
     *
     * @param condition        if true, the filter is applied
     * @param supervisorUserId the supervisor user ID
     * @return this query for method chaining
     */
    SupervisionQuery supervisorUserId(boolean condition, String supervisorUserId);

    /**
     * Filter by task instance ID.
     *
     * @param taskInstanceId the task instance ID
     * @return this query for method chaining
     */
    SupervisionQuery taskInstanceId(String taskInstanceId);

    /**
     * Filter by multiple task instance IDs.
     *
     * @param taskInstanceIds the list of task instance IDs
     * @return this query for method chaining
     */
    SupervisionQuery taskInstanceIdIn(List<String> taskInstanceIds);

    /**
     * Filter by supervision type.
     *
     * @param supervisionType the supervision type
     * @return this query for method chaining
     */
    SupervisionQuery supervisionType(String supervisionType);

    /**
     * Conditionally filter by supervision type.
     *
     * @param condition       if true, the filter is applied
     * @param supervisionType the supervision type
     * @return this query for method chaining
     */
    SupervisionQuery supervisionType(boolean condition, String supervisionType);

    /**
     * Filter by supervision status.
     *
     * @param status the supervision status
     * @return this query for method chaining
     */
    SupervisionQuery supervisionStatus(String status);

    /**
     * Conditionally filter by supervision status.
     *
     * @param condition if true, the filter is applied
     * @param status    the supervision status
     * @return this query for method chaining
     */
    SupervisionQuery supervisionStatus(boolean condition, String status);

    /**
     * Filter by supervision time range (start).
     *
     * @param startTime the start of supervision time range
     * @return this query for method chaining
     */
    SupervisionQuery supervisionTimeAfter(Date startTime);

    /**
     * Filter by supervision time range (end).
     *
     * @param endTime the end of supervision time range
     * @return this query for method chaining
     */
    SupervisionQuery supervisionTimeBefore(Date endTime);

    // ============ Ordering ============

    /**
     * Order by supervision instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    SupervisionQuery orderBySupervisionId();

    /**
     * Order by close time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    SupervisionQuery orderByCloseTime();
}

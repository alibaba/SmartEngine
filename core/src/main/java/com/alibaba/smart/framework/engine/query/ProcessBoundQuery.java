package com.alibaba.smart.framework.engine.query;

import java.util.List;

/**
 * Shared query interface for entities bound to a process instance.
 * Provides common filtering by processInstanceId and shared ordering.
 * Inspired by Flowable's TaskInfoQuery shared interface pattern.
 *
 * @param <Q> the query type itself for method chaining
 * @param <T> the result entity type
 */
public interface ProcessBoundQuery<Q extends ProcessBoundQuery<Q, T>, T> extends Query<Q, T> {

    /**
     * Filter by process instance ID.
     *
     * @param processInstanceId the process instance ID
     * @return this query for method chaining
     */
    Q processInstanceId(String processInstanceId);

    /**
     * Conditionally filter by process instance ID.
     *
     * @param condition         if true, the filter is applied
     * @param processInstanceId the process instance ID
     * @return this query for method chaining
     */
    Q processInstanceId(boolean condition, String processInstanceId);

    /**
     * Filter by multiple process instance IDs.
     *
     * @param processInstanceIds the list of process instance IDs
     * @return this query for method chaining
     */
    Q processInstanceIdIn(List<String> processInstanceIds);

    /**
     * Order by create time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    Q orderByCreateTime();

    /**
     * Order by modify time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    Q orderByModifyTime();

    /**
     * Set ascending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    Q asc();

    /**
     * Set descending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    Q desc();
}

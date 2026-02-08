package com.alibaba.smart.framework.engine.query;

import java.util.List;

/**
 * Base interface for fluent query API.
 * Provides common query operations like pagination and sorting.
 *
 * @param <Q> the query type itself for method chaining
 * @param <T> the result entity type
 * @author SmartEngine Team
 */
public interface Query<Q extends Query<Q, T>, T> {

    // ============ Pagination ============

    /**
     * Set the page offset (0-based).
     *
     * @param offset page offset, starting from 0
     * @return this query for method chaining
     */
    Q pageOffset(int offset);

    /**
     * Set the page size.
     *
     * @param size maximum number of results to return
     * @return this query for method chaining
     */
    Q pageSize(int size);

    // ============ Tenant ============

    /**
     * Filter by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return this query for method chaining
     */
    Q tenantId(String tenantId);

    /**
     * Conditionally filter by tenant ID.
     *
     * @param condition if true, the tenantId filter is applied
     * @param tenantId  the tenant ID
     * @return this query for method chaining
     */
    Q tenantId(boolean condition, String tenantId);

    /**
     * Conditionally set the page offset (0-based).
     *
     * @param condition if true, the offset is applied
     * @param offset    page offset, starting from 0
     * @return this query for method chaining
     */
    Q pageOffset(boolean condition, int offset);

    /**
     * Conditionally set the page size.
     *
     * @param condition if true, the size is applied
     * @param size      maximum number of results to return
     * @return this query for method chaining
     */
    Q pageSize(boolean condition, int size);

    /**
     * Exclude tenant filtering. Useful for cross-tenant queries.
     *
     * @return this query for method chaining
     */
    Q withoutTenantId();

    // ============ Execution methods ============

    /**
     * Execute the query and return all matching results.
     *
     * @return list of matching entities
     */
    List<T> list();

    /**
     * Execute the query with pagination and return matching results.
     *
     * @param offset page offset (0-based)
     * @param limit  maximum number of results
     * @return list of matching entities
     */
    List<T> listPage(int offset, int limit);

    /**
     * Execute the query and return the single result.
     *
     * @return the single result, or null if not found
     * @throws IllegalStateException if more than one result is found
     */
    T singleResult();

    /**
     * Execute the query and return the count of matching entities.
     *
     * @return the count
     */
    long count();
}

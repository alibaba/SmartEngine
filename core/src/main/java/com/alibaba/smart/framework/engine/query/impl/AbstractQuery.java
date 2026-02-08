package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.query.OrderSpec;
import com.alibaba.smart.framework.engine.query.Query;

/**
 * Abstract base implementation for fluent query API.
 * Provides common functionality for pagination, ordering, and execution.
 *
 * @param <Q> the query type itself for method chaining
 * @param <T> the result entity type
 * @author SmartEngine Team
 */
public abstract class AbstractQuery<Q extends Query<Q, T>, T> implements Query<Q, T> {

    protected ProcessEngineConfiguration processEngineConfiguration;

    protected Integer pageOffset;
    protected Integer pageSize;
    protected String tenantId;
    protected boolean excludeTenant = false;

    protected List<OrderSpec> orderBySpecs = new ArrayList<>();
    protected String currentOrderByProperty;
    protected String currentOrderByColumn;

    protected AbstractQuery(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @SuppressWarnings("unchecked")
    protected Q self() {
        return (Q) this;
    }

    // ============ Pagination ============

    @Override
    public Q pageOffset(int offset) {
        this.pageOffset = offset;
        return self();
    }

    @Override
    public Q pageSize(int size) {
        this.pageSize = size;
        return self();
    }

    // ============ Tenant ============

    @Override
    public Q tenantId(String tenantId) {
        this.tenantId = tenantId;
        return self();
    }

    @Override
    public Q tenantId(boolean condition, String tenantId) {
        if (condition) {
            this.tenantId = tenantId;
        }
        return self();
    }

    @Override
    public Q pageOffset(boolean condition, int offset) {
        if (condition) {
            this.pageOffset = offset;
        }
        return self();
    }

    @Override
    public Q pageSize(boolean condition, int size) {
        if (condition) {
            this.pageSize = size;
        }
        return self();
    }

    @Override
    public Q withoutTenantId() {
        this.excludeTenant = true;
        return self();
    }

    // ============ Ordering helpers ============

    /**
     * Set up an order by with the given property and column names.
     * The actual direction (asc/desc) will be set by a subsequent call.
     *
     * @param propertyName the Java property name
     * @param columnName   the database column name
     * @return this query for method chaining
     */
    protected Q orderBy(String propertyName, String columnName) {
        this.currentOrderByProperty = propertyName;
        this.currentOrderByColumn = columnName;
        return self();
    }

    /**
     * Apply ascending order to the current order by specification.
     *
     * @return this query for method chaining
     */
    protected Q applyAsc() {
        if (currentOrderByColumn != null) {
            orderBySpecs.add(new OrderSpec(currentOrderByProperty, currentOrderByColumn, OrderSpec.Direction.ASC));
            currentOrderByProperty = null;
            currentOrderByColumn = null;
        }
        return self();
    }

    /**
     * Apply descending order to the current order by specification.
     *
     * @return this query for method chaining
     */
    protected Q applyDesc() {
        if (currentOrderByColumn != null) {
            orderBySpecs.add(new OrderSpec(currentOrderByProperty, currentOrderByColumn, OrderSpec.Direction.DESC));
            currentOrderByProperty = null;
            currentOrderByColumn = null;
        }
        return self();
    }

    // ============ Execution methods ============

    @Override
    public List<T> list() {
        List<T> results = executeList();
        return results != null ? results : new ArrayList<>();
    }

    @Override
    public List<T> listPage(int offset, int limit) {
        this.pageOffset = offset;
        this.pageSize = limit;
        List<T> results = executeList();
        return results != null ? results : new ArrayList<>();
    }

    @Override
    public T singleResult() {
        List<T> results = list();
        if (results.isEmpty()) {
            return null;
        }
        if (results.size() > 1) {
            throw new IllegalStateException("Query returned " + results.size() + " results instead of max 1");
        }
        return results.get(0);
    }

    @Override
    public long count() {
        return executeCount();
    }

    // ============ Abstract methods to be implemented by subclasses ============

    /**
     * Execute the list query.
     *
     * @return the list of results
     */
    protected abstract List<T> executeList();

    /**
     * Execute the count query.
     *
     * @return the count of results
     */
    protected abstract long executeCount();

    // ============ Getters for subclasses ============

    public List<OrderSpec> getOrderBySpecs() {
        return orderBySpecs;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getTenantId() {
        return tenantId;
    }

    protected ProcessEngineConfiguration getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }
}

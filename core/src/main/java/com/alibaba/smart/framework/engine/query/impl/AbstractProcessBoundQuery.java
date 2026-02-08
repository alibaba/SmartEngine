package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.query.ProcessBoundQuery;

/**
 * Abstract base for queries bound to process instances.
 * Provides common processInstanceId filtering and ordering.
 *
 * @param <Q> the query type itself for method chaining
 * @param <T> the result entity type
 */
public abstract class AbstractProcessBoundQuery<Q extends ProcessBoundQuery<Q, T>, T>
        extends AbstractQuery<Q, T> implements ProcessBoundQuery<Q, T> {

    protected String processInstanceId;
    protected List<String> processInstanceIds;

    protected AbstractProcessBoundQuery(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public Q processInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return self();
    }

    @Override
    public Q processInstanceId(boolean condition, String processInstanceId) {
        if (condition) {
            this.processInstanceId = processInstanceId;
        }
        return self();
    }

    @Override
    public Q processInstanceIdIn(List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return self();
    }

    @Override
    public Q orderByCreateTime() {
        return orderBy("gmtCreate", "gmt_create");
    }

    @Override
    public Q orderByModifyTime() {
        return orderBy("gmtModified", "gmt_modified");
    }

    @Override
    public Q asc() {
        return applyAsc();
    }

    @Override
    public Q desc() {
        return applyDesc();
    }

    /**
     * Build process instance ID list for query param.
     * Helper for subclasses' buildQueryParam() methods.
     *
     * @return the process instance ID list, or null if not set
     */
    protected List<String> buildProcessInstanceIdList() {
        if (processInstanceId != null) {
            List<String> ids = new ArrayList<>();
            ids.add(processInstanceId);
            return ids;
        } else if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            return processInstanceIds;
        }
        return null;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public List<String> getProcessInstanceIds() {
        return processInstanceIds;
    }
}

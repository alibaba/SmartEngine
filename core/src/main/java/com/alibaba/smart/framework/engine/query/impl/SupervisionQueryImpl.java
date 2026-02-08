package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.SupervisionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.SupervisionInstance;
import com.alibaba.smart.framework.engine.query.SupervisionQuery;
import com.alibaba.smart.framework.engine.service.param.query.SupervisionQueryParam;

/**
 * Implementation of SupervisionQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class SupervisionQueryImpl extends AbstractProcessBoundQuery<SupervisionQuery, SupervisionInstance>
        implements SupervisionQuery {

    private SupervisionInstanceStorage supervisionInstanceStorage;

    // Filter conditions
    private String supervisionId;
    private String supervisorUserId;
    private String taskInstanceId;
    private List<String> taskInstanceIds;
    private String supervisionType;
    private String status;
    private Date supervisionStartTime;
    private Date supervisionEndTime;

    public SupervisionQueryImpl(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
        this.supervisionInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, SupervisionInstanceStorage.class);
    }

    // ============ Filter conditions ============

    @Override
    public SupervisionQuery supervisionId(String supervisionId) {
        this.supervisionId = supervisionId;
        return this;
    }

    @Override
    public SupervisionQuery supervisorUserId(String supervisorUserId) {
        this.supervisorUserId = supervisorUserId;
        return this;
    }

    @Override
    public SupervisionQuery supervisorUserId(boolean condition, String supervisorUserId) {
        if (condition) {
            this.supervisorUserId = supervisorUserId;
        }
        return this;
    }

    @Override
    public SupervisionQuery taskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
        return this;
    }

    @Override
    public SupervisionQuery taskInstanceIdIn(List<String> taskInstanceIds) {
        this.taskInstanceIds = taskInstanceIds;
        return this;
    }

    @Override
    public SupervisionQuery supervisionType(String supervisionType) {
        this.supervisionType = supervisionType;
        return this;
    }

    @Override
    public SupervisionQuery supervisionType(boolean condition, String supervisionType) {
        if (condition) {
            this.supervisionType = supervisionType;
        }
        return this;
    }

    @Override
    public SupervisionQuery supervisionStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public SupervisionQuery supervisionStatus(boolean condition, String status) {
        if (condition) {
            this.status = status;
        }
        return this;
    }

    @Override
    public SupervisionQuery supervisionTimeAfter(Date startTime) {
        this.supervisionStartTime = startTime;
        return this;
    }

    @Override
    public SupervisionQuery supervisionTimeBefore(Date endTime) {
        this.supervisionEndTime = endTime;
        return this;
    }

    // ============ Ordering ============

    @Override
    public SupervisionQuery orderBySupervisionId() {
        return orderBy("id", "id");
    }

    @Override
    public SupervisionQuery orderByCloseTime() {
        return orderBy("closeTime", "close_time");
    }

    // ============ Execution ============

    @Override
    protected List<SupervisionInstance> executeList() {
        SupervisionQueryParam param = buildQueryParam();
        return supervisionInstanceStorage.findSupervisionList(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        SupervisionQueryParam param = buildQueryParam();
        Long count = supervisionInstanceStorage.countSupervision(param, processEngineConfiguration);
        return count != null ? count : 0L;
    }

    /**
     * Build SupervisionQueryParam from the fluent query settings.
     */
    private SupervisionQueryParam buildQueryParam() {
        SupervisionQueryParam param = new SupervisionQueryParam();

        // Set ID filter
        if (supervisionId != null) {
            param.setId(Long.parseLong(supervisionId));
        }

        // Set supervisor filter
        param.setSupervisorUserId(supervisorUserId);

        // Set task instance filter
        if (taskInstanceId != null) {
            List<String> ids = new ArrayList<>();
            ids.add(taskInstanceId);
            param.setTaskInstanceIdList(ids);
        } else if (taskInstanceIds != null && !taskInstanceIds.isEmpty()) {
            param.setTaskInstanceIdList(taskInstanceIds);
        }

        // Set process instance filter (from base class)
        List<String> processInstanceIdList = buildProcessInstanceIdList();
        if (processInstanceIdList != null) {
            param.setProcessInstanceIdList(processInstanceIdList);
        }

        // Set other filters
        param.setSupervisionType(supervisionType);
        param.setStatus(status);
        param.setSupervisionStartTime(supervisionStartTime);
        param.setSupervisionEndTime(supervisionEndTime);

        // Set pagination
        param.setPageOffset(pageOffset);
        param.setPageSize(pageSize);
        param.setTenantId(tenantId);

        // Set order by specs
        param.setOrderBySpecs(orderBySpecs);

        return param;
    }
}

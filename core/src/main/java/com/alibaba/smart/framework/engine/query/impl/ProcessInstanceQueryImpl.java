package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.query.ProcessInstanceQuery;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

/**
 * Implementation of ProcessInstanceQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class ProcessInstanceQueryImpl extends AbstractQuery<ProcessInstanceQuery, ProcessInstance>
        implements ProcessInstanceQuery {

    private ProcessInstanceStorage processInstanceStorage;

    // Filter conditions
    private String processInstanceId;
    private List<String> processInstanceIds;
    private String startUserId;
    private String status;
    private String processDefinitionType;
    private String processDefinitionIdAndVersion;
    private String parentInstanceId;
    private String bizUniqueId;
    private Date startTimeAfter;
    private Date startTimeBefore;
    private Date completeTimeStart;
    private Date completeTimeEnd;
    private List<String> processDefinitionTypeList;

    public ProcessInstanceQueryImpl(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
        this.processInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, ProcessInstanceStorage.class);
    }

    // ============ Filter conditions ============

    @Override
    public ProcessInstanceQuery processInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    @Override
    public ProcessInstanceQuery processInstanceId(boolean condition, String processInstanceId) {
        if (condition) {
            this.processInstanceId = processInstanceId;
        }
        return this;
    }

    @Override
    public ProcessInstanceQuery processInstanceIdIn(List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }

    @Override
    public ProcessInstanceQuery startedBy(String startUserId) {
        this.startUserId = startUserId;
        return this;
    }

    @Override
    public ProcessInstanceQuery startedBy(boolean condition, String startUserId) {
        if (condition) {
            this.startUserId = startUserId;
        }
        return this;
    }

    @Override
    public ProcessInstanceQuery processStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public ProcessInstanceQuery processStatus(boolean condition, String status) {
        if (condition) {
            this.status = status;
        }
        return this;
    }

    @Override
    public ProcessInstanceQuery processDefinitionType(String processDefinitionType) {
        this.processDefinitionType = processDefinitionType;
        return this;
    }

    @Override
    public ProcessInstanceQuery processDefinitionType(boolean condition, String processDefinitionType) {
        if (condition) {
            this.processDefinitionType = processDefinitionType;
        }
        return this;
    }

    @Override
    public ProcessInstanceQuery processDefinitionIdAndVersion(String processDefinitionIdAndVersion) {
        this.processDefinitionIdAndVersion = processDefinitionIdAndVersion;
        return this;
    }

    @Override
    public ProcessInstanceQuery parentInstanceId(String parentInstanceId) {
        this.parentInstanceId = parentInstanceId;
        return this;
    }

    @Override
    public ProcessInstanceQuery bizUniqueId(String bizUniqueId) {
        this.bizUniqueId = bizUniqueId;
        return this;
    }

    @Override
    public ProcessInstanceQuery bizUniqueId(boolean condition, String bizUniqueId) {
        if (condition) {
            this.bizUniqueId = bizUniqueId;
        }
        return this;
    }

    @Override
    public ProcessInstanceQuery startedAfter(Date startTimeAfter) {
        this.startTimeAfter = startTimeAfter;
        return this;
    }

    @Override
    public ProcessInstanceQuery startedBefore(Date startTimeBefore) {
        this.startTimeBefore = startTimeBefore;
        return this;
    }

    @Override
    public ProcessInstanceQuery completedAfter(Date completeTimeStart) {
        this.completeTimeStart = completeTimeStart;
        return this;
    }

    @Override
    public ProcessInstanceQuery completedBefore(Date completeTimeEnd) {
        this.completeTimeEnd = completeTimeEnd;
        return this;
    }

    @Override
    public ProcessInstanceQuery processDefinitionTypeIn(List<String> types) {
        this.processDefinitionTypeList = types != null ? new ArrayList<String>(types) : null;
        return this;
    }

    @Override
    public ProcessInstanceQuery involvedUser(String userId) {
        this.startUserId = userId;
        return this;
    }

    // ============ Ordering ============

    @Override
    public ProcessInstanceQuery orderByProcessInstanceId() {
        return orderBy("id", "id");
    }

    @Override
    public ProcessInstanceQuery orderByStartTime() {
        return orderBy("gmtCreate", "gmt_create");
    }

    @Override
    public ProcessInstanceQuery orderByModifyTime() {
        return orderBy("gmtModified", "gmt_modified");
    }

    @Override
    public ProcessInstanceQuery orderByCompleteTime() {
        return orderBy("completeTime", "complete_time");
    }

    @Override
    public ProcessInstanceQuery asc() {
        return applyAsc();
    }

    @Override
    public ProcessInstanceQuery desc() {
        return applyDesc();
    }

    // ============ Execution ============

    @Override
    protected List<ProcessInstance> executeList() {
        ProcessInstanceQueryParam param = buildQueryParam();
        return processInstanceStorage.queryProcessInstanceList(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        ProcessInstanceQueryParam param = buildQueryParam();
        Long count = processInstanceStorage.count(param, processEngineConfiguration);
        return count != null ? count : 0L;
    }

    /**
     * Build ProcessInstanceQueryParam from the fluent query settings.
     */
    private ProcessInstanceQueryParam buildQueryParam() {
        ProcessInstanceQueryParam param = new ProcessInstanceQueryParam();

        // Set ID filter
        if (processInstanceId != null) {
            List<String> ids = new ArrayList<>();
            ids.add(processInstanceId);
            param.setProcessInstanceIdList(ids);
        } else if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            param.setProcessInstanceIdList(processInstanceIds);
        }

        // Set other filters
        param.setStartUserId(startUserId);
        param.setStatus(status);
        param.setProcessDefinitionType(processDefinitionType);
        param.setProcessDefinitionIdAndVersion(processDefinitionIdAndVersion);
        param.setParentInstanceId(parentInstanceId);
        param.setBizUniqueId(bizUniqueId);
        param.setProcessStartTime(startTimeAfter);
        param.setProcessEndTime(startTimeBefore);
        param.setCompleteTimeStart(completeTimeStart);
        param.setCompleteTimeEnd(completeTimeEnd);
        // processDefinitionTypeList takes precedence over single type
        if (processDefinitionTypeList != null && !processDefinitionTypeList.isEmpty()) {
            param.setProcessDefinitionTypeList(processDefinitionTypeList);
        }

        // Set pagination
        param.setPageOffset(pageOffset);
        param.setPageSize(pageSize);
        if (!excludeTenant) {
            param.setTenantId(tenantId);
        }

        // Set order by specs
        param.setOrderBySpecs(orderBySpecs);

        return param;
    }
}

package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.query.TaskQuery;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * Implementation of TaskQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class TaskQueryImpl extends AbstractQuery<TaskQuery, TaskInstance> implements TaskQuery {

    private TaskInstanceStorage taskInstanceStorage;

    // Filter conditions
    private String taskInstanceId;
    private String processInstanceId;
    private List<String> processInstanceIds;
    private String activityInstanceId;
    private String processDefinitionType;
    private String processDefinitionActivityId;
    private String status;
    private String claimUserId;
    private String tag;
    private String extension;
    private Integer priority;
    private String comment;
    private String title;
    private Date completeTimeStart;
    private Date completeTimeEnd;

    public TaskQueryImpl(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
        this.taskInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, TaskInstanceStorage.class);
    }

    // ============ Filter conditions ============

    @Override
    public TaskQuery taskInstanceId(String taskInstanceId) {
        this.taskInstanceId = taskInstanceId;
        return this;
    }

    @Override
    public TaskQuery processInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    @Override
    public TaskQuery processInstanceIdIn(List<String> processInstanceIds) {
        this.processInstanceIds = processInstanceIds;
        return this;
    }

    @Override
    public TaskQuery activityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
        return this;
    }

    @Override
    public TaskQuery processDefinitionType(String processDefinitionType) {
        this.processDefinitionType = processDefinitionType;
        return this;
    }

    @Override
    public TaskQuery processDefinitionActivityId(String processDefinitionActivityId) {
        this.processDefinitionActivityId = processDefinitionActivityId;
        return this;
    }

    @Override
    public TaskQuery taskStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public TaskQuery taskAssignee(String claimUserId) {
        this.claimUserId = claimUserId;
        return this;
    }

    @Override
    public TaskQuery taskTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public TaskQuery taskExtension(String extension) {
        this.extension = extension;
        return this;
    }

    @Override
    public TaskQuery taskPriority(Integer priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public TaskQuery taskComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public TaskQuery taskTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public TaskQuery completeTimeAfter(Date completeTimeStart) {
        this.completeTimeStart = completeTimeStart;
        return this;
    }

    @Override
    public TaskQuery completeTimeBefore(Date completeTimeEnd) {
        this.completeTimeEnd = completeTimeEnd;
        return this;
    }

    // ============ Ordering ============

    @Override
    public TaskQuery orderByTaskId() {
        return orderBy("id", "id");
    }

    @Override
    public TaskQuery orderByCreateTime() {
        return orderBy("gmtCreate", "gmt_create");
    }

    @Override
    public TaskQuery orderByModifyTime() {
        return orderBy("gmtModified", "gmt_modified");
    }

    @Override
    public TaskQuery orderByClaimTime() {
        return orderBy("claimTime", "claim_time");
    }

    @Override
    public TaskQuery orderByCompleteTime() {
        return orderBy("completeTime", "complete_time");
    }

    @Override
    public TaskQuery orderByPriority() {
        return orderBy("priority", "priority");
    }

    @Override
    public TaskQuery asc() {
        return applyAsc();
    }

    @Override
    public TaskQuery desc() {
        return applyDesc();
    }

    // ============ Execution ============

    @Override
    protected List<TaskInstance> executeList() {
        TaskInstanceQueryParam param = buildQueryParam();
        return taskInstanceStorage.findTaskList(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        TaskInstanceQueryParam param = buildQueryParam();
        Long count = taskInstanceStorage.count(param, processEngineConfiguration);
        return count != null ? count : 0L;
    }

    /**
     * Build TaskInstanceQueryParam from the fluent query settings.
     */
    private TaskInstanceQueryParam buildQueryParam() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();

        // Set ID filter
        if (taskInstanceId != null) {
            param.setId(Long.parseLong(taskInstanceId));
        }

        // Set process instance filter
        if (processInstanceId != null) {
            List<String> ids = new ArrayList<>();
            ids.add(processInstanceId);
            param.setProcessInstanceIdList(ids);
        } else if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            param.setProcessInstanceIdList(processInstanceIds);
        }

        // Set other filters
        param.setActivityInstanceId(activityInstanceId);
        param.setProcessDefinitionType(processDefinitionType);
        param.setProcessDefinitionActivityId(processDefinitionActivityId);
        param.setStatus(status);
        param.setClaimUserId(claimUserId);
        param.setTag(tag);
        param.setExtension(extension);
        param.setPriority(priority);
        param.setComment(comment);
        param.setTitle(title);
        param.setCompleteTimeStart(completeTimeStart);
        param.setCompleteTimeEnd(completeTimeEnd);

        // Set pagination
        param.setPageOffset(pageOffset);
        param.setPageSize(pageSize);
        param.setTenantId(tenantId);

        // Set order by specs
        param.setOrderBySpecs(orderBySpecs);

        return param;
    }
}

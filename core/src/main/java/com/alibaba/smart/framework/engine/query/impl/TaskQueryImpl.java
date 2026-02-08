package com.alibaba.smart.framework.engine.query.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.dialect.Dialect;
import com.alibaba.smart.framework.engine.dialect.DialectRegistry;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.query.TaskQuery;
import com.alibaba.smart.framework.engine.service.param.query.JsonCondition;
import com.alibaba.smart.framework.engine.service.param.query.JsonInCondition;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * Implementation of TaskQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class TaskQueryImpl extends AbstractProcessBoundQuery<TaskQuery, TaskInstance> implements TaskQuery {

    private TaskInstanceStorage taskInstanceStorage;

    // Filter conditions
    private String taskInstanceId;
    private String activityInstanceId;
    private String processDefinitionType;
    private String processDefinitionActivityId;
    private String status;
    private List<String> statusList;
    private String claimUserId;
    private String tag;
    private String extension;
    private Integer priority;
    private String comment;
    private String title;
    private Date completeTimeStart;
    private Date completeTimeEnd;
    private Date createTimeStart;
    private Date createTimeEnd;
    private String titleLike;
    private boolean unassigned;
    private List<String> processDefinitionTypeList;
    private String claimUserIdLike;
    private Integer minPriority;
    private Integer maxPriority;
    private String domainCode;
    private List<String> domainCodeList;
    private String domainCodeLike;
    private Map<String, String> jsonExactFilters = new LinkedHashMap<String, String>();
    private Map<String, List<String>> jsonInFilters = new LinkedHashMap<String, List<String>>();
    private Map<String, String> jsonLikeFilters = new LinkedHashMap<String, String>();

    private static final Pattern VALID_JSON_KEY = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_.]*$");

    // Candidate assignee filters (triggers assignee JOIN query path)
    private String candidateUserId;
    private List<String> candidateGroupIds;

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
    public TaskQuery processDefinitionType(boolean condition, String processDefinitionType) {
        if (condition) {
            this.processDefinitionType = processDefinitionType;
        }
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
    public TaskQuery taskStatus(boolean condition, String status) {
        if (condition) {
            this.status = status;
        }
        return this;
    }

    @Override
    public TaskQuery taskStatusIn(List<String> statuses) {
        this.statusList = statuses;
        return this;
    }

    @Override
    public TaskQuery taskStatusIn(String... statuses) {
        this.statusList = Arrays.asList(statuses);
        return this;
    }

    @Override
    public TaskQuery taskAssignee(String claimUserId) {
        this.claimUserId = claimUserId;
        return this;
    }

    @Override
    public TaskQuery taskAssignee(boolean condition, String claimUserId) {
        if (condition) {
            this.claimUserId = claimUserId;
        }
        return this;
    }

    @Override
    public TaskQuery taskCandidateUser(String userId) {
        this.candidateUserId = userId;
        return this;
    }

    @Override
    public TaskQuery taskCandidateGroup(String groupId) {
        this.candidateGroupIds = Collections.singletonList(groupId);
        return this;
    }

    @Override
    public TaskQuery taskCandidateGroupIn(List<String> groupIds) {
        this.candidateGroupIds = groupIds;
        return this;
    }

    @Override
    public TaskQuery taskCandidateOrGroup(String userId, List<String> groupIds) {
        this.candidateUserId = userId;
        this.candidateGroupIds = groupIds;
        return this;
    }

    @Override
    public TaskQuery taskTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public TaskQuery taskTag(boolean condition, String tag) {
        if (condition) {
            this.tag = tag;
        }
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
    public TaskQuery taskTitle(boolean condition, String title) {
        if (condition) {
            this.title = title;
        }
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

    @Override
    public TaskQuery createdAfter(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
        return this;
    }

    @Override
    public TaskQuery createdBefore(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
        return this;
    }

    @Override
    public TaskQuery taskTitleLike(String titleLike) {
        this.titleLike = titleLike;
        return this;
    }

    @Override
    public TaskQuery taskTitleLike(boolean condition, String titleLike) {
        if (condition) {
            this.titleLike = titleLike;
        }
        return this;
    }

    @Override
    public TaskQuery taskUnassigned() {
        this.unassigned = true;
        return this;
    }

    @Override
    public TaskQuery processDefinitionTypeIn(List<String> types) {
        this.processDefinitionTypeList = types != null ? new ArrayList<String>(types) : null;
        return this;
    }

    @Override
    public TaskQuery taskAssigneeLike(String claimUserIdLike) {
        this.claimUserIdLike = claimUserIdLike;
        return this;
    }

    @Override
    public TaskQuery taskMinPriority(Integer minPriority) {
        this.minPriority = minPriority;
        return this;
    }

    @Override
    public TaskQuery taskMaxPriority(Integer maxPriority) {
        this.maxPriority = maxPriority;
        return this;
    }

    // ============ Domain code filters ============

    @Override
    public TaskQuery domainCode(String domainCode) {
        this.domainCode = domainCode;
        return this;
    }

    @Override
    public TaskQuery domainCode(boolean condition, String domainCode) {
        if (condition) {
            this.domainCode = domainCode;
        }
        return this;
    }

    @Override
    public TaskQuery domainCodeIn(List<String> domainCodes) {
        this.domainCodeList = domainCodes != null ? new ArrayList<String>(domainCodes) : null;
        return this;
    }

    @Override
    public TaskQuery domainCodeLike(String domainCodeLike) {
        this.domainCodeLike = domainCodeLike;
        return this;
    }

    // ============ Extra JSON filters ============

    @Override
    public TaskQuery extraJson(String key, String value) {
        validateJsonKey(key);
        jsonExactFilters.put(key, value);
        return this;
    }

    @Override
    public TaskQuery extraJsonIn(String key, List<String> values) {
        validateJsonKey(key);
        jsonInFilters.put(key, values);
        return this;
    }

    @Override
    public TaskQuery extraJsonLike(String key, String pattern) {
        validateJsonKey(key);
        jsonLikeFilters.put(key, pattern);
        return this;
    }

    private void validateJsonKey(String key) {
        if (key == null || !VALID_JSON_KEY.matcher(key).matches()) {
            throw new IllegalArgumentException("Invalid JSON key: " + key
                    + ". Only [a-zA-Z_][a-zA-Z0-9_.]* is allowed.");
        }
    }

    // ============ Ordering ============

    @Override
    public TaskQuery orderByTaskId() {
        return orderBy("id", "id");
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

    // ============ Execution ============

    /**
     * Check if candidate assignee filters are set, which requires the assignee JOIN query path.
     */
    private boolean isCandidateQuery() {
        return candidateUserId != null || (candidateGroupIds != null && !candidateGroupIds.isEmpty());
    }

    @Override
    protected List<TaskInstance> executeList() {
        if (isCandidateQuery()) {
            TaskInstanceQueryByAssigneeParam param = buildAssigneeQueryParam();
            return taskInstanceStorage.findTaskListByAssignee(param, processEngineConfiguration);
        }
        TaskInstanceQueryParam param = buildQueryParam();
        return taskInstanceStorage.findTaskList(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        if (isCandidateQuery()) {
            TaskInstanceQueryByAssigneeParam param = buildAssigneeQueryParam();
            Long count = taskInstanceStorage.countTaskListByAssignee(param, processEngineConfiguration);
            return count != null ? count : 0L;
        }
        TaskInstanceQueryParam param = buildQueryParam();
        Long count = taskInstanceStorage.count(param, processEngineConfiguration);
        return count != null ? count : 0L;
    }

    /**
     * Build TaskInstanceQueryByAssigneeParam for the assignee JOIN query path.
     */
    private TaskInstanceQueryByAssigneeParam buildAssigneeQueryParam() {
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();

        param.setAssigneeUserId(candidateUserId);
        // Treat empty group list as null to avoid empty IN() SQL syntax error
        param.setAssigneeGroupIdList(
            candidateGroupIds != null && !candidateGroupIds.isEmpty() ? candidateGroupIds : null);
        param.setProcessDefinitionType(processDefinitionType);

        // Set process instance ID list
        List<String> processInstanceIdList = buildProcessInstanceIdList();
        if (processInstanceIdList != null) {
            List<Long> longIds = new ArrayList<Long>(processInstanceIdList.size());
            for (String id : processInstanceIdList) {
                longIds.add(Long.parseLong(id));
            }
            param.setProcessInstanceIdList(longIds);
        }

        // statusList takes precedence, else single status
        if (statusList != null && !statusList.isEmpty()) {
            param.setStatus(statusList.get(0));
        } else {
            param.setStatus(status);
        }

        // domain_code filters
        param.setDomainCode(domainCode);
        param.setDomainCodeList(domainCodeList);
        param.setDomainCodeLike(domainCodeLike);

        // Build JSON conditions
        buildJsonConditionsForAssignee(param);

        // Set pagination and tenant
        param.setPageOffset(pageOffset);
        param.setPageSize(pageSize);
        if (!excludeTenant) {
            param.setTenantId(tenantId);
        }

        return param;
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

        // Set process instance filter (from base class)
        List<String> processInstanceIdList = buildProcessInstanceIdList();
        if (processInstanceIdList != null) {
            param.setProcessInstanceIdList(processInstanceIdList);
        }

        // Set other filters
        param.setActivityInstanceId(activityInstanceId);
        param.setProcessDefinitionType(processDefinitionType);
        param.setProcessDefinitionActivityId(processDefinitionActivityId);
        // statusList takes precedence over single status
        if (statusList != null && !statusList.isEmpty()) {
            param.setStatusList(statusList);
        } else {
            param.setStatus(status);
        }
        param.setClaimUserId(claimUserId);
        param.setTag(tag);
        param.setExtension(extension);
        param.setPriority(priority);
        param.setComment(comment);
        param.setTitle(title);
        param.setTitleLike(titleLike);
        param.setCompleteTimeStart(completeTimeStart);
        param.setCompleteTimeEnd(completeTimeEnd);
        param.setCreateTimeStart(createTimeStart);
        param.setCreateTimeEnd(createTimeEnd);
        param.setUnassigned(unassigned ? Boolean.TRUE : null);
        param.setClaimUserIdLike(claimUserIdLike);
        param.setMinPriority(minPriority);
        param.setMaxPriority(maxPriority);
        // processDefinitionTypeList takes precedence over single type
        if (processDefinitionTypeList != null && !processDefinitionTypeList.isEmpty()) {
            param.setProcessDefinitionTypeList(processDefinitionTypeList);
        }

        // domain_code filters
        param.setDomainCode(domainCode);
        param.setDomainCodeList(domainCodeList);
        param.setDomainCodeLike(domainCodeLike);

        // Build JSON conditions
        buildJsonConditions(param);

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

    private Dialect resolveDialect() {
        Dialect dialect = processEngineConfiguration.getDialect();
        if (dialect == null) {
            dialect = DialectRegistry.getInstance().getDefaultDialect();
        }
        return dialect;
    }

    private void buildJsonConditions(TaskInstanceQueryParam param) {
        if (jsonExactFilters.isEmpty() && jsonInFilters.isEmpty() && jsonLikeFilters.isEmpty()) {
            return;
        }
        Dialect dialect = resolveDialect();

        if (!jsonExactFilters.isEmpty()) {
            List<JsonCondition> conditions = new ArrayList<JsonCondition>();
            for (Map.Entry<String, String> e : jsonExactFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                conditions.add(new JsonCondition(expr, e.getValue()));
            }
            param.setJsonConditions(conditions);
        }

        if (!jsonInFilters.isEmpty()) {
            List<JsonInCondition> inConditions = new ArrayList<JsonInCondition>();
            for (Map.Entry<String, List<String>> e : jsonInFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                inConditions.add(new JsonInCondition(expr, e.getValue()));
            }
            param.setJsonInConditions(inConditions);
        }

        if (!jsonLikeFilters.isEmpty()) {
            List<JsonCondition> likeConditions = new ArrayList<JsonCondition>();
            for (Map.Entry<String, String> e : jsonLikeFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                likeConditions.add(new JsonCondition(expr, e.getValue()));
            }
            param.setJsonLikeConditions(likeConditions);
        }
    }

    private void buildJsonConditionsForAssignee(TaskInstanceQueryByAssigneeParam param) {
        if (jsonExactFilters.isEmpty() && jsonInFilters.isEmpty() && jsonLikeFilters.isEmpty()) {
            return;
        }
        Dialect dialect = resolveDialect();

        if (!jsonExactFilters.isEmpty()) {
            List<JsonCondition> conditions = new ArrayList<JsonCondition>();
            for (Map.Entry<String, String> e : jsonExactFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                conditions.add(new JsonCondition(expr, e.getValue()));
            }
            param.setJsonConditions(conditions);
        }

        if (!jsonInFilters.isEmpty()) {
            List<JsonInCondition> inConditions = new ArrayList<JsonInCondition>();
            for (Map.Entry<String, List<String>> e : jsonInFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                inConditions.add(new JsonInCondition(expr, e.getValue()));
            }
            param.setJsonInConditions(inConditions);
        }

        if (!jsonLikeFilters.isEmpty()) {
            List<JsonCondition> likeConditions = new ArrayList<JsonCondition>();
            for (Map.Entry<String, String> e : jsonLikeFilters.entrySet()) {
                String expr = dialect.jsonExtractText("task.extra", e.getKey());
                likeConditions.add(new JsonCondition(expr, e.getValue()));
            }
            param.setJsonLikeConditions(likeConditions);
        }
    }
}

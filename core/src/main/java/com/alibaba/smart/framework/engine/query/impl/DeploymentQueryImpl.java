package com.alibaba.smart.framework.engine.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.storage.DeploymentInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.query.DeploymentQuery;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

/**
 * Implementation of DeploymentQuery fluent API.
 *
 * @author SmartEngine Team
 */
public class DeploymentQueryImpl extends AbstractQuery<DeploymentQuery, DeploymentInstance>
        implements DeploymentQuery {

    private DeploymentInstanceStorage deploymentInstanceStorage;

    // Filter conditions
    private String deploymentInstanceId;
    private String processDefinitionVersion;
    private String processDefinitionType;
    private String processDefinitionCode;
    private String processDefinitionName;
    private String processDefinitionNameLike;
    private String processDefinitionDescLike;
    private String deploymentUserId;
    private String deploymentStatus;
    private String logicStatus;

    public DeploymentQueryImpl(ProcessEngineConfiguration processEngineConfiguration) {
        super(processEngineConfiguration);
        this.deploymentInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, DeploymentInstanceStorage.class);
    }

    // ============ Filter conditions ============

    @Override
    public DeploymentQuery deploymentInstanceId(String deploymentInstanceId) {
        this.deploymentInstanceId = deploymentInstanceId;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionVersion(String version) {
        this.processDefinitionVersion = version;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionType(String processDefinitionType) {
        this.processDefinitionType = processDefinitionType;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionType(boolean condition, String processDefinitionType) {
        if (condition) {
            this.processDefinitionType = processDefinitionType;
        }
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionCode(String processDefinitionCode) {
        this.processDefinitionCode = processDefinitionCode;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionNameLike(String processDefinitionNameLike) {
        this.processDefinitionNameLike = processDefinitionNameLike;
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionNameLike(boolean condition, String processDefinitionNameLike) {
        if (condition) {
            this.processDefinitionNameLike = processDefinitionNameLike;
        }
        return this;
    }

    @Override
    public DeploymentQuery processDefinitionDescLike(String processDefinitionDescLike) {
        this.processDefinitionDescLike = processDefinitionDescLike;
        return this;
    }

    @Override
    public DeploymentQuery deploymentUserId(String deploymentUserId) {
        this.deploymentUserId = deploymentUserId;
        return this;
    }

    @Override
    public DeploymentQuery deploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
        return this;
    }

    @Override
    public DeploymentQuery deploymentStatus(boolean condition, String deploymentStatus) {
        if (condition) {
            this.deploymentStatus = deploymentStatus;
        }
        return this;
    }

    @Override
    public DeploymentQuery logicStatus(String logicStatus) {
        this.logicStatus = logicStatus;
        return this;
    }

    // ============ Ordering ============

    @Override
    public DeploymentQuery orderByDeploymentId() {
        return orderBy("id", "id");
    }

    @Override
    public DeploymentQuery orderByCreateTime() {
        return orderBy("gmtCreate", "gmt_create");
    }

    @Override
    public DeploymentQuery orderByModifyTime() {
        return orderBy("gmtModified", "gmt_modified");
    }

    @Override
    public DeploymentQuery asc() {
        return applyAsc();
    }

    @Override
    public DeploymentQuery desc() {
        return applyDesc();
    }

    // ============ Execution ============

    @Override
    protected List<DeploymentInstance> executeList() {
        DeploymentInstanceQueryParam param = buildQueryParam();
        return deploymentInstanceStorage.findByPage(param, processEngineConfiguration);
    }

    @Override
    protected long executeCount() {
        DeploymentInstanceQueryParam param = buildQueryParam();
        return deploymentInstanceStorage.count(param, processEngineConfiguration);
    }

    /**
     * Build DeploymentInstanceQueryParam from the fluent query settings.
     */
    private DeploymentInstanceQueryParam buildQueryParam() {
        DeploymentInstanceQueryParam param = new DeploymentInstanceQueryParam();

        if (deploymentInstanceId != null) {
            param.setId(Long.parseLong(deploymentInstanceId));
        }

        param.setProcessDefinitionVersion(processDefinitionVersion);
        param.setProcessDefinitionType(processDefinitionType);
        param.setProcessDefinitionCode(processDefinitionCode);
        param.setProcessDefinitionName(processDefinitionName);
        param.setProcessDefinitionNameLike(processDefinitionNameLike);
        param.setProcessDefinitionDescLike(processDefinitionDescLike);
        param.setDeploymentUserId(deploymentUserId);
        param.setDeploymentStatus(deploymentStatus);
        param.setLogicStatus(logicStatus);

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

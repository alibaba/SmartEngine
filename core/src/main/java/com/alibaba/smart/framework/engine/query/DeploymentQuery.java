package com.alibaba.smart.framework.engine.query;

import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;

/**
 * Fluent query interface for DeploymentInstance.
 * Provides type-safe method chaining for building deployment queries.
 *
 * <p>Example usage:
 * <pre>{@code
 * List<DeploymentInstance> deployments = smartEngine.createDeploymentQuery()
 *     .processDefinitionType("approval")
 *     .deploymentStatus(DeploymentStatusConstant.ACTIVE)
 *     .orderByModifyTime().desc()
 *     .listPage(0, 10);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public interface DeploymentQuery extends Query<DeploymentQuery, DeploymentInstance> {

    // ============ Filter conditions ============

    /**
     * Filter by deployment instance ID.
     *
     * @param deploymentInstanceId the deployment instance ID
     * @return this query for method chaining
     */
    DeploymentQuery deploymentInstanceId(String deploymentInstanceId);

    /**
     * Filter by process definition version.
     *
     * @param version the process definition version
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionVersion(String version);

    /**
     * Filter by process definition type.
     *
     * @param processDefinitionType the process definition type
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionType(String processDefinitionType);

    /**
     * Conditionally filter by process definition type.
     *
     * @param condition             if true, the filter is applied
     * @param processDefinitionType the process definition type
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionType(boolean condition, String processDefinitionType);

    /**
     * Filter by process definition code (key).
     *
     * @param processDefinitionCode the process definition code
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionCode(String processDefinitionCode);

    /**
     * Filter by process definition name (exact match).
     *
     * @param processDefinitionName the process definition name
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionName(String processDefinitionName);

    /**
     * Filter by process definition name (fuzzy match, LIKE %nameLike%).
     *
     * @param processDefinitionNameLike the name pattern
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionNameLike(String processDefinitionNameLike);

    /**
     * Conditionally filter by process definition name (fuzzy match).
     *
     * @param condition                if true, the filter is applied
     * @param processDefinitionNameLike the name pattern
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionNameLike(boolean condition, String processDefinitionNameLike);

    /**
     * Filter by process definition description (fuzzy match, LIKE %descLike%).
     *
     * @param processDefinitionDescLike the description pattern
     * @return this query for method chaining
     */
    DeploymentQuery processDefinitionDescLike(String processDefinitionDescLike);

    /**
     * Filter by deployment user ID.
     *
     * @param deploymentUserId the user who deployed
     * @return this query for method chaining
     */
    DeploymentQuery deploymentUserId(String deploymentUserId);

    /**
     * Filter by deployment status.
     *
     * @param deploymentStatus the deployment status
     * @return this query for method chaining
     * @see com.alibaba.smart.framework.engine.constant.DeploymentStatusConstant
     */
    DeploymentQuery deploymentStatus(String deploymentStatus);

    /**
     * Conditionally filter by deployment status.
     *
     * @param condition        if true, the filter is applied
     * @param deploymentStatus the deployment status
     * @return this query for method chaining
     */
    DeploymentQuery deploymentStatus(boolean condition, String deploymentStatus);

    /**
     * Filter by logic status.
     *
     * @param logicStatus the logic status
     * @return this query for method chaining
     * @see com.alibaba.smart.framework.engine.constant.LogicStatusConstant
     */
    DeploymentQuery logicStatus(String logicStatus);

    // ============ Ordering ============

    /**
     * Order by deployment instance ID.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    DeploymentQuery orderByDeploymentId();

    /**
     * Order by create time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    DeploymentQuery orderByCreateTime();

    /**
     * Order by modify time.
     *
     * @return this query for method chaining (call asc() or desc() next)
     */
    DeploymentQuery orderByModifyTime();

    /**
     * Set ascending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    DeploymentQuery asc();

    /**
     * Set descending order for the previous orderBy call.
     *
     * @return this query for method chaining
     */
    DeploymentQuery desc();
}

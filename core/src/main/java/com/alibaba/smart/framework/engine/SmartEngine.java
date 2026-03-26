package com.alibaba.smart.framework.engine;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.NotificationCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.SupervisionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.VariableCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.NotificationQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;
import com.alibaba.smart.framework.engine.service.query.SupervisionQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;
import com.alibaba.smart.framework.engine.query.DeploymentQuery;
import com.alibaba.smart.framework.engine.query.NotificationQuery;
import com.alibaba.smart.framework.engine.query.ProcessInstanceQuery;
import com.alibaba.smart.framework.engine.query.SupervisionQuery;
import com.alibaba.smart.framework.engine.query.TaskQuery;

/**
 * @author 高海军 帝奇
 * 业务处理引擎和服务编排引擎，此类是各种 API 的顶层入口。
 *
 * 下面的 API 方法名称比较简单，不需要额外说明 。 详细注释见各个方法的返回值对象说明。
 *
 */
public interface SmartEngine {


    ProcessEngineConfiguration getProcessEngineConfiguration();

    void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration);

    RepositoryCommandService getRepositoryCommandService();

    RepositoryQueryService getRepositoryQueryService();

    DeploymentCommandService getDeploymentCommandService();

    DeploymentQueryService getDeploymentQueryService();

    ProcessCommandService getProcessCommandService();

    ProcessQueryService getProcessQueryService();

    ActivityQueryService getActivityQueryService();

    ExecutionCommandService getExecutionCommandService();

    TaskCommandService getTaskCommandService();

    VariableCommandService getVariableCommandService();


    ExecutionQueryService getExecutionQueryService();

    TaskQueryService getTaskQueryService();

    VariableQueryService getVariableQueryService();

    TaskAssigneeQueryService getTaskAssigneeQueryService();

    SupervisionCommandService getSupervisionCommandService();

    SupervisionQueryService getSupervisionQueryService();

    NotificationCommandService getNotificationCommandService();

    NotificationQueryService getNotificationQueryService();

    // ============ Fluent Query API ============

    /**
     * Create a new task query for fluent API style querying.
     *
     * <p>Example usage:
     * <pre>{@code
     * List<TaskInstance> tasks = smartEngine.createTaskQuery()
     *     .processInstanceId("12345")
     *     .taskAssignee("user001")
     *     .taskStatus(TaskInstanceConstant.PENDING)
     *     .orderByCreateTime().desc()
     *     .listPage(0, 10);
     * }</pre>
     *
     * @return a new TaskQuery instance
     */
    TaskQuery createTaskQuery();

    /**
     * Create a new process instance query for fluent API style querying.
     *
     * <p>Example usage:
     * <pre>{@code
     * List<ProcessInstance> processes = smartEngine.createProcessQuery()
     *     .processDefinitionType("approval")
     *     .startedBy("user001")
     *     .processStatus("running")
     *     .orderByStartTime().desc()
     *     .listPage(0, 10);
     * }</pre>
     *
     * @return a new ProcessInstanceQuery instance
     */
    ProcessInstanceQuery createProcessQuery();

    /**
     * Create a new supervision query for fluent API style querying.
     *
     * <p>Example usage:
     * <pre>{@code
     * List<SupervisionInstance> supervisions = smartEngine.createSupervisionQuery()
     *     .supervisorUserId("supervisor001")
     *     .supervisionStatus("active")
     *     .orderByCreateTime().desc()
     *     .listPage(0, 10);
     * }</pre>
     *
     * @return a new SupervisionQuery instance
     */
    SupervisionQuery createSupervisionQuery();

    /**
     * Create a new notification query for fluent API style querying.
     *
     * <p>Example usage:
     * <pre>{@code
     * List<NotificationInstance> notifications = smartEngine.createNotificationQuery()
     *     .receiverUserId("user001")
     *     .readStatus("unread")
     *     .orderByCreateTime().desc()
     *     .listPage(0, 10);
     * }</pre>
     *
     * @return a new NotificationQuery instance
     */
    NotificationQuery createNotificationQuery();

    /**
     * Create a new deployment query for fluent API style querying.
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
     * @return a new DeploymentQuery instance
     */
    DeploymentQuery createDeploymentQuery();


    void init(ProcessEngineConfiguration processEngineConfiguration);

    void destroy();


}
